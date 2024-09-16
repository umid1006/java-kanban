package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class MockTaskManager implements TaskManager {
    private int id;

    public MockTaskManager(int id) {
        this.id = id;
    }

    @Override
    public int generateNewId() {
        return id;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>();
    }

    @Override
    public java.util.List<Subtask> getAllSubtasks() {
        return new ArrayList<>();
    }

    @Override
    public java.util.List<Epic> getAllEpics() {
        return new ArrayList<>();
    }

    @Override
    public Task getTaskById(int id) {
        return null;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        return null;
    }

    @Override
    public Epic getEpicBySubtaskId(int id) {
        return null;
    }

    @Override
    public void deleteAllTasks() {
        // No-op
    }

    @Override
    public void deleteAllSubtasks() {
        // No-op
    }

    @Override
    public void deleteAllEpics() {
        // No-op
    }

    @Override
    public int addNewTask(Task task) {
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        return epic.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        return subtask.getId();
    }

    @Override
    public void updateTask(Task updatedTask) {
        // No-op
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        // No-op
    }

    @Override
    public void deleteTaskById(int taskId) {
        // No-op
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        // No-op
    }

    @Override
    public void deleteEpicById(int epicId) {
        // No-op
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return new ArrayList<>();
    }

    @Override
    public List<TaskManager> getHistory() {
        return new ArrayList<>();
    }

}