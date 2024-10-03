package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }

    private final Path file; // Поле для хранения файла

    // Переопределяем модифицирующие операции
    @Override
    public int addNewTask(Task task) {
        if (task == null) {
            return 0;
        }
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

// Метод для сохранения текущего состояния менеджера в файл
public void save() {
    try (FileWriter writer = new FileWriter(file.toFile(), StandardCharsets.UTF_8)) {
        writer.write("id,type,name,status,description,epic\n");

        for (Task task : getAllTasks()) {
            writer.write(String.format("%d,Task,%s,%s,%s,\n",
                    task.getId(), task.getName(), task.getStatus().name(), task.getDescription()));
        }
        for (Epic epic : getAllEpics()) {
            writer.write(String.format("%d,Epic,%s,%s,%s,\n", // Corrected line for Epic
                    epic.getId(), epic.getName(), epic.getStatus().name(), epic.getDescription()));
        }
        for (Subtask subtask : getAllSubtasks()) {
            writer.write(String.format("%d,Subtask,%s,%s,%s,%d\n", // Corrected line for Subtask
                    subtask.getId(), subtask.getName(), subtask.getStatus().name(), subtask.getDescription(), subtask.getEpicId()));
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
        if (lines.size() > 1) {
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                // Parse data from the string
                int id = Integer.parseInt(parts[0]);
                String type = parts[1]; // Get the task type
                String name = parts[2];
                Status status = Status.valueOf(parts[3]); // Parse the status correctly
                String description = parts[4];

                // Determine the task type and create the corresponding object
                switch (type) {
                    case "Task":
                        Task task = new Task(name, description, status, id);
                        manager.addNewTask(task);
                        break;
                    case "Subtask":
                        int epicId = Integer.parseInt(parts[5]);
                        Subtask subtask = new Subtask(name, description, status, epicId);
                        manager.addNewSubtask(subtask);
                        break;
                    case "Epic":
                        Epic epic = new Epic(name, description, status);
                        manager.addNewEpic(epic);
                        break;
                    default:
                        System.err.println("Invalid line format: " + line);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error loading from file: " + e.getMessage());
    }
    return manager;
}
}