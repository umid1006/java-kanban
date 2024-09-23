package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    List<TaskManager> getHistory();

    void addTask(Task task);

    void remove(int id);
}
