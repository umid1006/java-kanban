import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private static final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;

    public TaskManager() {
        tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public static int generateTaskId() {
        // Implement logic to generate unique task ID (consider UUIDs)
        int nextTaskId = 1;
        while (tasks.containsKey(nextTaskId)) {
            nextTaskId++;
        }
        return nextTaskId;
    }

    // Create methods for each task type (Task, Subtask, Epic)
    public static void createTask(String name, String description, String status) {
        Task task = new Task(name, description, status);
        int taskId = generateTaskId();
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void createSubtask(String name, String description, String status, int epicId) {
        Subtask subtask = new Subtask(name, description, status, epicId);
        int subtaskId = generateTaskId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
    }

    public void createEpic(String name, String description, String status) {
        Epic epic = new Epic(name, description, status);
        int epicId = generateTaskId();
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    // Update methods (consider separate methods for different properties or full update)
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            // Trigger epic status update if applicable (see updateEpicStatus)
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            return epic;
        }
        return null;
    }

    // Delete methods for each task type
    public boolean deleteTask(int id) {
        return tasks.remove(id) != null;
    }

    public boolean deleteSubtask(int id) {
        Subtask removedSubtask = subtasks.remove(id);
        if (removedSubtask != null) {
            // Update epic status if the subtask belonged to an epic
            updateEpicStatus(epics.get(removedSubtask.getEpicId()));
        }
        return removedSubtask != null;
    }

    public boolean deleteEpic(int id) {
        return epics.remove(id) != null;
    }

    // Get methods for each task type (by ID or list)
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values()); // Convert HashMap values to List
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values()); // Convert HashMap values to List
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values()); // Convert HashMap values to List
    }

    // Method to update epic status based on subtask statuses
    private void updateEpicStatus(Epic epic) {
        // Implement logic to calculate epic status based on subtask statuses (NEW, IN_
    }

    public void createSubtask(Subtask subtask1) {
    }
}

