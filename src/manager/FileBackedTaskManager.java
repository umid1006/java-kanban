package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    // Переопределяем модифицирующие операции
    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        super.updateSubtask(updatedSubtask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }


private final Path file; // Поле для хранения файла

public FileBackedTaskManager(Path file) {
    this.file = file;
}

// Метод для сохранения текущего состояния менеджера в файл
private void save() {
    try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8)) {
        // Записываем заголовок
        writer.write("id,type,name,status,description,epic\n");

        // Записываем задачи
        for (Task task : getAllTasks()) {
            writer.write(task.toString() + "\n");
        }
        for (Epic epic : getAllEpics()) {
            writer.write(epic.toString() + "\n");
        }
        for (Subtask subtask : getAllSubtasks()) {
            writer.write(subtask.toString() + "\n");
        }

    } catch (IOException e) {
        System.err.println("Ошибка сохранения в файл: " + e.getMessage());
    }
}

// Статический метод для загрузки данных из файла
public static FileBackedTaskManager loadFromFile(Path file) {
    FileBackedTaskManager manager = new FileBackedTaskManager(file);
    try {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.size() > 1) { // Проверяем, есть ли данные в файле
            for (int i = 1; i < lines.size(); i++) { // Начинаем с 1, чтобы пропустить заголовок
                String line = lines.get(i);
                String[] parts = line.split(",");
                // ... (логика для парсинга данных из строки и добавления задач в менеджер)
            }
        }
    } catch (IOException e) {
        System.err.println("Ошибка загрузки из файла: " + e.getMessage());
    }
    return manager;
}



}

