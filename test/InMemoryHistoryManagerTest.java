import manager.InMemoryHistoryManager;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void testGetHistory() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        manager.addTask(task1);
        manager.addTask(task2);

        List<TaskManager> history = manager.getHistory();
        assertEquals(2, history.size());
        assertInstanceOf(Task.class, history.get(0));
        assertInstanceOf(Task.class, history.get(1));
    }

    @Test
    public void testAddTask() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        manager.addTask(task1);
        manager.addTask(task2);

        List<TaskManager> history = manager.getHistory();
        assertEquals(2, history.size());
        assertSame(task1, history.get(0));
        assertSame(task2, history.get(1));
    }

    @Test
    public void testRemoveNode() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        manager.removeNode(manager.nodesById.get(2)); // Remove task2

        List<TaskManager> history = manager.getHistory();
        assertEquals(2, history.size());
        assertSame(task1, history.get(0));
        assertSame(task3, history.get(1));
        assertNull(manager.nodesById.get(2));
    }
}