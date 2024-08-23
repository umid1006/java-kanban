package manager;

import java.util.List;

public interface HistoryManager {

    List<TaskManager> getHistory();

    void addTask(TaskManager task);
}
