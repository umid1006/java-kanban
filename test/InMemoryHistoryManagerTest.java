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
    void addTask() {
    }

    @Test
    void remove() {
    }
}