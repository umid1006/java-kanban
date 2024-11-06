package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager, Gson gson) { // Add Gson parameter
        super(taskManager, gson); // Call superclass constructor
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            int responseCode;
            String response = "";
            try {
                responseCode = switch (method) {
                    case "GET" -> {
                        response = getMethod(exchange, path);
                        yield response != null ? 200 : 404;
                    }
                    case "POST" -> {
                        response = postMethod(exchange, path);
                        yield response.equals("201") ? 201 : 400;
                    }
                    case "DELETE" -> {
                        response = deleteMethod(exchange, path);
                        yield response.equals("200") ? 200 : 404;
                    }
                    default -> 405;
                };
            } catch (NotFoundException e) {
                responseCode = 404;
                response = e.getMessage();
            }
            assert response != null;
            writeResponse(exchange, response, responseCode);
        } catch (Exception ignored) {

        } finally {
            exchange.close();
        }
    }

    private String getMethod(HttpExchange httpExchange, String path) throws NotFoundException {
        String response = null;
        if (path.equals("/tasks")) {
            List<Task> tasks = taskManager.getAllTasks();
            response = gson.toJson(tasks);
        } else if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                response = gson.toJson(task);
            }
        }
        return response;
    }

    private String postMethod(HttpExchange httpExchange, String path) throws Exception {
        String response = "201";
        if (path.equals("/tasks")) {
            String body = readText(httpExchange);
            Task task = gson.fromJson(body, Task.class);
            if (task.getStartTime() != null && taskManager.isTaskIntersectsWithOthers(task)) {
                return "406"; // Задача пересекается с другими
            }
            taskManager.addNewTask(task);
        } else if (path.matches("/tasks/\\d+")) {
            String body = readText(httpExchange);
            Task task = gson.fromJson(body, Task.class);
            if (task.getStartTime() != null && taskManager.isTaskIntersectsWithOthers(task)) {
                return "406"; // Задача пересекается с другими
            }
            taskManager.updateTask(task);
        }
        return response;
    }

    private String deleteMethod(HttpExchange httpExchange, String path) {
        String response = "200";
        if (path.equals("/tasks")) {
            taskManager.deleteAllTasks();
        } else if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
            taskManager.deleteTaskById(id);
        }
        return response;
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
        exchange.sendResponseHeaders(responseCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}