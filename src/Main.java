import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // Create tasks (provide name, description, and status as arguments)
        Task task1 = new Task("Task #1", "Task1 description", "NEW");
        Task task2 = new Task("Task #2", "Task2 description", "IN_PROGRESS");
        Epic epic1 = new Epic("Epic #1", "Epic1 description", "NEW");
        Epic epic2 = new Epic("Epic #2", "Epic2 description", "NEW");
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", "NEW", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask1 description", "NEW", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask #3-2", "Subtask1 description", "NEW", epic2.getId());

        // Add tasks using appropriate methods
        TaskManager.createTask(task1);
        TaskManager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        // Print all tasks
        printTasks(manager.getAllTasks());

        // Update task status
        Task retrievedTask = manager.getSubtaskById(task2.getId());
        retrievedTask.setStatus("DONE");
        manager.updateTask(retrievedTask);

        System.out.println("\nTasks after status update:");
        printTasks(manager.getAllTasks());
    }

    private static void printTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println(task);
            if (task instanceof Epic) {
                // Retrieve subtasks from TaskManager based on epicId
                List<Subtask> subtasks = manager.getSubtasksOfEpic(((Epic) task).getId());
                System.out.println("  Subtasks:");
                for (Subtask subtask : subtasks) {
                    System.out.println("    " + subtask);
                }
            }
        }
    }
}