package manager;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;

    private final LinkedList<TaskManager> history = new LinkedList<>();

    @Override
    public List<TaskManager> getHistory() {
        return new LinkedList<>(history);
    }

    @Override
    public void addTask(TaskManager task) {
        if (task == null) {
            return;
}
        history.add(task);
        if (history.size() > HISTORY_SIZE) {
            history.removeFirst();
        }
    }
}
