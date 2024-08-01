package logic;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int id = 1;

    public int generateNewId() {
        return id++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubTaskById(int id) {
        Task task = tasks.get(id);
        if (task instanceof Subtask) {
            return subtasks.get(id);
        }
        return null;  // Handle case where no task or non-model.Subtask is found
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic getEpicBySubtaskId(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return getEpicById(subtask.getEpicId());
        }
        return null; // Or throw an exception if preferred
    }

    public void deleteAllTasks() {
        tasks.clear(); // Assuming tasks are stored in a map
    }

    public void deleteAllSubtasks() {
        subtasks.clear(); // Remove subtasks from the map
        // Update epic subtask fields based on deleted subtasks
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear(); // Clear existing subtasks in the epic
        }
    }

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

    public int addNewTask(Task task) {
        int id = generateNewId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic) {
        int id = generateNewId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public Integer addNewSubtask(Subtask subtask) {
        int id = generateNewId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        return id;
    }

    public void updateTask(Task updatedTask) {
        int taskId = updatedTask.getId(); // Get ID from updated object
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, updatedTask); // Replace existing task with updated one
        } else {
            System.out.println("model.Task with ID " + taskId + " not found.");
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        int subtaskId = updatedSubtask.getId();

        if (subtasks.containsKey(subtaskId)) {
            Subtask oldSubtask = subtasks.get(subtaskId);
            subtasks.put(subtaskId, updatedSubtask);

            Epic updatedEpic = getEpicBySubtaskId(oldSubtask.getEpicId());
            if (updatedEpic != null) {
                String newStatus = String.valueOf(updatedEpic.recalculateEpicStatus());
                updatedEpic.setStatus(newStatus);
            } else {
                System.out.println("Epic not found for subtask " + subtaskId);
            }
        } else {
            System.out.println("Subtask with ID " + subtaskId + " not found.");
        }
    }


    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("model.Task with ID " + taskId + " not found.");
        }
    }

    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
        } else {
            System.out.println("model.Subtask with ID " + subtaskId + " not found.");
        }
    }

    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            epics.remove(epicId);
        } else {
            System.out.println("model.Epic with ID " + epicId + " not found.");
        }
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> matchingSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) { // Iterate over values in subtasks Map
            if (subtask.getEpicId() == epicId) {
                matchingSubtasks.add(subtask);
            }
        }
        return matchingSubtasks;
    }
    }













