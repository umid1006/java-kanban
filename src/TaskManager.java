import java.util.*;
import java.util.UUID;

public class TaskManager {

    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;
    private final UUID idGenerator = UUID.randomUUID();

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();

    }

    public List<Task> getAllTasks() {
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
            return (Subtask) task;
        }
        return null;  // Handle case where no task or non-Subtask is found
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void deleteAllTasks() {
        tasks.clear(); // Assuming tasks are stored in a map
    }

    public void deleteAllSubtasks() {
        subtasks.clear(); // Assuming subtasks are stored in a map
    }

    public void deleteAllEpics() {
        epics.clear(); // Assuming epics are stored in a map
        // Consider handling associated subtasks (e.g., delete or reassign)
    }

    public int addNewTask(Task task) {
        String uuidString = idGenerator.toString();
        int id = uuidString.hashCode(); // Convert String to int (potential collisions)
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic) {
        String uuidString = idGenerator.toString();
        int id = uuidString.hashCode(); // Convert String to int (potential collisions)
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public Integer addNewSubtask(Subtask subtask) {
        String uuidString = idGenerator.toString();
        int id = uuidString.hashCode(); // Convert String to int (potential collisions)
        subtask.setId(id);
        subtasks.put(id, subtask);
        return id;
    }

    public void updateTask(Task updatedTask) {
        int taskId = updatedTask.getId(); // Get ID from updated object
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, updatedTask); // Replace existing task with updated one
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        int subtaskId = updatedSubtask.getId(); // Get ID from updated object
        if (subtasks.containsKey(subtaskId)) {
            subtasks.put(subtaskId, updatedSubtask); // Replace existing subtask with updated one
        } else {
            System.out.println("Subtask with ID " + subtaskId + " not found.");
        }
    }

    public void updateEpic(Epic updatedEpic) {
        int epicId = updatedEpic.getId(); // Get ID from updated object
        if (epics.containsKey(epicId)) {
            epics.put(epicId, updatedEpic); // Replace existing epic with updated one
        } else {
            System.out.println("Epic with ID " + epicId + " not found.");
        }
    }

    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
        } else {
            System.out.println("Subtask with ID " + subtaskId + " not found.");
        }
    }

    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            epics.remove(epicId);
        } else {
            System.out.println("Epic with ID " + epicId + " not found.");
        }
    }

    public List<Subtask> getSubtasksForEpic(int epicId) {
        List<Subtask> matchingSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) { // Iterate over values in subtasks Map
            if (subtask.getEpicId() == epicId) {
                matchingSubtasks.add(subtask);
            }
        }
        return matchingSubtasks;
    }



    private void updateTaskStatus(Task task, String newStatus) {
        // Manager doesn't set status directly; it's based on task info
        // ... (Logic to determine task status based on task information)

        // Update task status in the system
        task.setStatus(newStatus);
    }

    private void updateEpicStatus(Epic epic) {
        // Check if all subtasks are NEW
        boolean allSubtasksNew = true;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId() && !subtask.getStatus().equals("NEW")) {
                allSubtasksNew = false;
                break;
            }
        }

        if (allSubtasksNew) {
            epic.setStatus("NEW");
        } else if (allSubtasksAreDone(epic)) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    private boolean allSubtasksAreDone(Epic epic) {
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId() && !subtask.getStatus().equals("DONE")) {
                return false;
            }
        }
        return true;
    }


}








