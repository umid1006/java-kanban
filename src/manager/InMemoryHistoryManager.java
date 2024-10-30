package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    public final HashMap<Integer, Node> nodesById = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void addTask(Task task) {
        int id = task.getId();

        // Проверяем, есть ли уже задача с таким ID
        if (nodesById.containsKey(id)) {
            // Обновляем существующую задачу
            Node existingNode = nodesById.get(id);
            existingNode.task = task;
            return; // Выходим из метода, так как задача уже добавлена
        }

        // Создаем новый узел и добавляем его в конец списка
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        nodesById.put(id, newNode);
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = nodesById.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    public static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task) {
            this.task = task;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;

        }
        if (node == head) {
            head = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        }
        nodesById.remove(node.task.getId());
    }
}
