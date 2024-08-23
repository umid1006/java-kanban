package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Task task = tasks.get(id);
        historyManager.addTask((TaskManager) tasks);

        return task;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addTask((TaskManager) subtasks);
        if (task instanceof Subtask) {
            return subtasks.get(id);
        }
        return null;  // Handle case where no task or non-model.Subtask is found
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addTask((TaskManager) epics);
        return epics.get(id);
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

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear(); // Remove subtasks from the map
        // Update epic subtask fields based on deleted subtasks
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear(); // Clear existing subtasks in the epic
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear(); // Remove epics from the map
        // Iterate over all epics and remove associated subtasks
        for (Epic epic : epics.values()) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId() == epic.getId()) {
                    subtasks.remove(subtask.getId());
                }
            }
        }
    }

    @Override
    public int addNewTask(Task task) {
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
        int id = generateNewId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        return id;
    }

    @Override
    public void updateTask(Task updatedTask) {
        int taskId = updatedTask.getId(); // Get ID from updated object
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, updatedTask); // Replace existing task with updated one
        } else {
            System.out.println("model.Task with ID " + taskId + " not found.");
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
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
        ArrayList<Subtask> matchingSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) { // Iterate over values in subtasks Map
            if (subtask.getEpicId() == epicId) {
                matchingSubtasks.add(subtask);
            }
        }
        return matchingSubtasks;
    }

    public List<TaskManager> getHistory() {
    return historyManager.getHistory();
    }
    }













