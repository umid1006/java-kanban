package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {
        TaskManager task;
        Node prev;
        Node next;

        public Node(TaskManager task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    public final HashMap<Integer, Node> nodesById = new HashMap<>();

    public void addTask(TaskManager task) {
        int id = task.generateNewId();
        // Если задача уже есть, удаляем старый узел
        Node existingNode = nodesById.get(id);
        if (existingNode != null) {
            removeNode(existingNode);
        }

        // Создаем новый узел и добавляем его в конец списка
        Node newNode = new Node(task);
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

    }

    void removeNode(Node node) {
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

    public List<TaskManager> getHistory() {
        List<TaskManager> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }
}
