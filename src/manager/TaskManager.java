package manager;

import model.Epic;
import model.Subtask;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int generateNewId();

    ArrayList<model.Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    model.Task getTaskById(int id);

    Subtask getSubTaskById(int id);

    Epic getEpicById(int id);

    Epic getEpicBySubtaskId(int id);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    int addNewTask(model.Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(model.Task updatedTask);

    void updateSubtask(Subtask updatedSubtask);

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subtaskId);

    void deleteEpicById(int epicId);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    List<TaskManager> getHistory();

}
