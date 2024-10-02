import manager.InMemoryHistoryManager;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void testGetHistory() {

    }

    @Test
    void testAddTask_NewTask() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание задачи 1");

        // Добавляем задачу
        manager.addTask(task);

        // Получаем историю
        List<Task> history = manager.getHistory();

        // Проверяем размер истории
        assertEquals(1, history.size());

        // Проверяем содержимое истории
        assertEquals(task, history.getFirst());
    }

    @Test
    void testRemove() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        manager.addTask(task1);
        manager.addTask(task2);

        manager.remove(1);

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }
}