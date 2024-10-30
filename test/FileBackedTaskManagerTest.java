import manager.FileBackedTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private Path file;

    @BeforeEach
    void setUp() throws IOException {
        file = Files.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void loadFromFile_emptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void loadFromFile_multipleTasks() {
        manager.addNewTask(new Task("Task 1", "Description 1", Status.NEW));
        manager.addNewEpic(new Epic("Epic 1", "Epic Description 1", Status.NEW));
        manager.addNewSubtask(new Subtask("Subtask 1", "Subtask Description 1", Status.NEW, 1)); // Assuming Epic with ID 1 exists
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
    }

    @Test
    void addNewTask() {
        Task task = new Task("New Task", "Description", Status.NEW);
        int taskId = manager.addNewTask(task);
        assertTrue(taskId > 0);
        assertEquals(task, manager.getTaskById(taskId));
    }

    @Test
    void updateTask() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        int taskId = manager.addNewTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(taskId).getStatus());
    }

    @Test
    void deleteTaskById() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        int taskId = manager.addNewTask(task);
        manager.deleteTaskById(taskId);
        assertNull(manager.getTaskById(taskId));
    }

    @Test
    void save_and_load_multipleTasks() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Epic epic = new Epic("Epic 1", "Epic Description 1", Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", Status.NEW, 1); // Assuming Epic with ID 1 exists

        manager.addNewTask(task);
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Assert that the loaded tasks are equal to the original tasks
        assertEquals(task, loadedManager.getTaskById(task.getId()));
        assertEquals(epic, loadedManager.getEpicById(epic.getId()));
        assertEquals(subtask, loadedManager.getSubTaskById(subtask.getId()));

        // Assert that the number of tasks is correct
        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
    }

}