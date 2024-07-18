import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // Create tasks (provide name, description, and status as arguments)
        Task task1 = new Task("Task #1", "Task1 description", "NEW");
        Task task2 = new Task("Task #2", "Task2 description", "IN_PROGRESS");
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Epic1 description", "NEW");
        Epic epic2 = new Epic("Epic #2", "Epic2 description", "NEW");
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", "NEW", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask1 description", "NEW", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask #3-2", "Subtask1 description", "NEW", epic2.getId());
        final Integer subtaskId1 = manager.addNewSubtask(subtask1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3);

        // Add tasks using appropriate methods
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.addNewSubtask(subtask3);

        // Print all tasks
        printTasks(manager.getAllTasks(), manager);

        // Update task status
        Task retrievedTask = manager.getSubTaskById(task2.getId());
        retrievedTask.setStatus("DONE");
        manager.updateTask(retrievedTask);

        System.out.println("\nTasks after status update:");
        printTasks(manager.getAllTasks(), manager);
    }

    private static void printTasks(List<Task> tasks, TaskManager manager) {
        for (Task task : tasks) {
            System.out.println(task);
            if (task instanceof Epic) {
                // Retrieve subtasks from TaskManager based on epicId
                List<Subtask> subtasks = manager.getSubtasksForEpic(((Epic) task).getId());
                System.out.println("  Subtasks:");
                for (Subtask subtask : subtasks) {
                    System.out.println("    " + subtask);
                }
            }
        }
    }
}