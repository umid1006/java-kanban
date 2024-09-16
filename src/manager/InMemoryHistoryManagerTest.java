package manager;

import manager.InMemoryHistoryManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void testGetHistory() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        TaskManager task1 = new MockTaskManager(1);
        TaskManager task2 = new MockTaskManager(2);

        manager.addTask(task1);
        manager.addTask(task2);

        List<TaskManager> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testAddTask() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        TaskManager task1 = new MockTaskManager(1);
        TaskManager task2 = new MockTaskManager(1); // Same ID as task1

        manager.addTask(task1);
        manager.addTask(task2);

        List<TaskManager> history = manager.getHistory();
        assertEquals(1, history.size()); // Only the latest task should be present
        assertEquals(task2, history.getFirst());
    }

    @Test
    public void testRemoveNode() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        TaskManager task1 = new MockTaskManager(1);
        TaskManager task2 = new MockTaskManager(2);
        TaskManager task3 = new MockTaskManager(3);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        manager.removeNode(manager.nodesById.get(2)); // Remove task2

        List<TaskManager> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertNull(manager.nodesById.get(2)); // Ensure task2 is removed from the map
    }

    @BeforeEach
    void setUp() {
        TaskManager taskManager = new InMemoryTaskManager();
    }



}