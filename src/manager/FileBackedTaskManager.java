package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

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
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%d,%d\n", // Added duration and startTime
                        task.getId(),
                        "Task", // Type of the task
                        task.getName(),
                        task.getStatus().name(),
                        task.getDescription(),
                        "", // No epicId for tasks
                        task.getDuration(), // Duration in minutes
                        task.getStartTime() != null ? task.getStartTime().toEpochSecond(ZoneOffset.UTC) : 0 // Start time in epoch seconds
                ));
            }
            for (Epic epic : getAllEpics()) {
                epic.updateFields();
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%d,%d\n",
                        epic.getId(), // Added epic ID
                        "Epic", // Type of the task
                        epic.getName(),
                        epic.getStatus().name(),
                        epic.getDescription(),
                        "", // No epicId for epics
                        epic.getDuration(), // Duration in minutes
                        epic.getStartTime() != null ? epic.getStartTime().toEpochSecond(ZoneOffset.UTC) : 0 // Start time in epoch seconds
                ));
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(String.format("%d,%s,%s,%s,%s,%d,%d,%d\n",
                        subtask.getId(), // Added subtask ID
                        "Subtask", // Type of the task
                        subtask.getName(),
                        subtask.getStatus().name(),
                        subtask.getDescription(),
                        subtask.getEpicId(), // Added epicId for subtasks
                        subtask.getDuration(), // Duration in minutes
                        subtask.getStartTime() != null ? subtask.getStartTime().toEpochSecond(ZoneOffset.UTC) : 0 // Start time in epoch seconds
                ));
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения в файл: " + e.getMessage());
        }
    }    // Статический метод для загрузки данных из файла

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
                    long duration = Long.parseLong(parts[6]); // Parse duration
                    LocalDateTime startTime = !parts[7].equals("0")
                            ? LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(parts[7])), ZoneOffset.UTC)
                            : null; // Parse start time

                    // Determine the task type and create the corresponding object
                    switch (type) {
                        case "Task":
                            Task task = new Task(name, description, status);
                            task.setId(id); // Set the ID
                            task.setDuration(Duration.ofMinutes(duration).toMinutes());
                            task.setStartTime(startTime);
                            manager.addNewTask(task);
                            break;
                        case "Subtask":
                            int epicId = Integer.parseInt(parts[5]); // Get epicId for subtasks
                            Subtask subtask = new Subtask(name, description, status, epicId);
                            subtask.setId(id); // Set the ID
                            subtask.setDuration(Duration.ofMinutes(duration));
                            subtask.setStartTime(startTime);
                            manager.addNewSubtask(subtask);
                            break;
                        case "Epic":
                            Epic epic = new Epic(name, description, status);
                            epic.setId(id); // Set the ID
                            epic.setDuration(Duration.ofMinutes(duration).toMinutes());
                            epic.setStartTime(startTime);
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

    public List<Task> getPrioritizedTasks() {
        // Create a TreeSet with a comparator that sorts tasks by startTime
        TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder()))); // nulls last

        // Add all tasks and subtasks to the TreeSet
        prioritizedTasks.addAll(getAllTasks());
        prioritizedTasks.addAll(getAllSubtasks());

        return new ArrayList<>(prioritizedTasks); // Return as a List
    }
}