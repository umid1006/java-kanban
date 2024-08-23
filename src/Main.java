import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import manager.InMemoryHistoryManager;
import manager.HistoryManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();


        // Create tasks (provide name, description, and status as arguments)
        Task task1 = new Task("model.Task #1", "Task1 description", Status.NEW);
        Task task2 = new Task("model.Task #2", "Task2 description", Status.NEW);
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("model.Epic #1", "Epic1 description", Status.NEW);
        Epic epic2 = new Epic("model.Epic #2", "Epic2 description", Status.NEW);
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("model.Subtask #1-1", "Subtask1 description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("model.Subtask #2-1", "Subtask1 description", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("model.Subtask #3-2", "Subtask1 description", Status.NEW, epic1.getId());
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
        printTasks(manager.getAllTasks(), (InMemoryTaskManager) manager);

        List<TaskManager> history = manager.getHistory();
        for (TaskManager item : history) {
        System.out.println(item);
        }

        // Update task status
        Task retrievedTask = manager.getSubTaskById(task2.getId());
        if (retrievedTask != null) {
            retrievedTask.setStatus(Status.DONE);
            manager.updateTask(retrievedTask);
        } else {
            System.out.println("Subtask with ID " + task2.getId() + " not found.");
        }

        System.out.println("\nTasks after status update:");
        printTasks(manager.getAllTasks(), (InMemoryTaskManager) manager);
    }

    private static void printTasks(List<Task> tasks, InMemoryTaskManager manager) {
        for (Task task : tasks) {
            System.out.println(task);
            if (task instanceof Epic epic) {
                // Retrieve subtasks from Logic.TaskManager based on epicId
                List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
                System.out.println("  Subtasks:");
                for (Subtask subtask : subtasks) {
                    System.out.println("    " + subtask);
                }
            }
        }
    }
}