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
    public List<TaskManager> getHistory() {
        List<TaskManager> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        int id = task.getId();
        // Если задача уже есть, удаляем старый узел
        Node existingNode = nodesById.get(id);
        if (existingNode != null) {
            removeNode(existingNode);
        }
        // Создаем новый узел и добавляем его в конец списка
        Node newNode = new Node((TaskManager) task);
        if (tail != null) {
            tail.next = newNode;
            newNode.prev = tail;
        } else {
            head = newNode;
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

    private static class Node {
        TaskManager task;
        Node prev;
        Node next;

        private Node(TaskManager task) {
            this.task = task;
        }
    }

    public void removeNode(Node node) {
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
        nodesById.remove(node.task.generateNewId());
    }
}
