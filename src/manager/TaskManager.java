package manager;

import exception.NotFoundException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int generateNewId();

    ArrayList<model.Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getTaskById(int id) throws NotFoundException;

    Subtask getSubTaskById(int id) throws NotFoundException; // Добавляем throws NotFoundException

    Epic getEpicById(int id) throws NotFoundException; // Добавляем throws NotFoundException

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

    List<Task> getHistory();

}