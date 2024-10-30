package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getHistoryDefault();
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int id = 1;

    @Override
    public int generateNewId() {
        return id++;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = subtasks.get(id); // Correctly retrieve the Subtask object
        if (subtask != null) {
            historyManager.addTask(subtask); // Add the Subtask to history
        }
        return subtask; // Handle case where no task or non-model.Subtask is found
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id); // Correctly retrieve the Epic object from the HashMap
        if (epic != null) {
            historyManager.addTask(epic); // Add the Epic to the history
        }
        return epic;
    }

    @Override
    public Epic getEpicBySubtaskId(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return getEpicById(subtask.getEpicId());
        }
        return null; // Or throw an exception if preferred
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear(); // Assuming tasks are stored in a map
    }

    public void deleteAllSubtasks() {
        subtasks.clear(); // Remove subtasks from the map
        // Update epic subtask fields based on deleted subtasks
        epics.values().forEach(epic -> epic.getSubtaskIds().clear());
    }

    @Override
    public void deleteAllEpics() {
        epics.values().stream()
                .flatMap(epic -> subtasks.values().stream()
                        .filter(subtask -> subtask.getEpicId() == epic.getId()))
                .forEach(subtask -> subtasks.remove(subtask.getId()));
        epics.clear(); // Remove epics from the map after removing subtasks
    }

    @Override
    public int addNewTask(Task task) {
        if (task.getStartTime() != null && isTaskIntersectsWithOthers(task)) {
            System.out.println("Задача пересекается с другими задачами!");
            return -1; // Or throw an exception if preferred
        }
        int id = generateNewId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = generateNewId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && isTaskIntersectsWithOthers(subtask)) {
            System.out.println("Подзадача пересекается с другими задачами!");
            return -1; // Or throw an exception if preferred
        }
        int id = generateNewId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        return id;
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (updatedTask.getStartTime() != null && isTaskIntersectsWithOthers(updatedTask)) {
            System.out.println("Задача пересекается с другими задачами!");
            return; // Or throw an exception if preferred
        }
        int taskId = updatedTask.getId(); // Get ID from updated object
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, updatedTask); // Replace existing task with updated one
        } else {
            System.out.println("model.Task with ID " + taskId + " not found.");
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (updatedSubtask.getStartTime() != null && isTaskIntersectsWithOthers(updatedSubtask)) {
            System.out.println("Подзадача пересекается с другими задачами!");
            return; // Or throw an exception if preferred
        }
        int subtaskId = updatedSubtask.getId();

        if (subtasks.containsKey(subtaskId)) {
            Subtask oldSubtask = subtasks.get(subtaskId);
            subtasks.put(subtaskId, updatedSubtask);

            Epic updatedEpic = getEpicBySubtaskId(oldSubtask.getEpicId());
            if (updatedEpic != null) {
                String newStatus = String.valueOf(updatedEpic.recalculateEpicStatus());
                updatedEpic.setStatus(Status.valueOf(newStatus));
            } else {
                System.out.println("Epic not found for subtask " + subtaskId);
            }
        } else {
            System.out.println("Subtask with ID " + subtaskId + " not found.");
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("model.Task with ID " + taskId + " not found.");
        }
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
        } else {
            System.out.println("model.Subtask with ID " + subtaskId + " not found.");
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            epics.remove(epicId);
        } else {
            System.out.println("model.Epic with ID " + epicId + " not found.");
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return (ArrayList<Subtask>) subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private boolean isTaskIntersectsWithOthers(Task task) {
        return getAllTasks().stream()
                .filter(otherTask -> otherTask.getId() != task.getId() && otherTask.getStartTime() != null)
                .anyMatch(otherTask -> isIntervalOverlapping(
                        task.getStartTime(), task.getEndTime(),
                        otherTask.getStartTime(), otherTask.getEndTime()));
    }

    private boolean isIntervalOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);

    }
}