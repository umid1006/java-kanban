package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String response;

            switch (method) {
                case "GET":
                    response = getMethod(httpExchange);
                    sendText(httpExchange, Objects.requireNonNullElse(response, "Подзадача не найдена"));
                    break;
                case "POST":
                    response = postMethod(httpExchange);
                    if (response.equals("201")) {
                        httpExchange.sendResponseHeaders(201, -1);
                    } else {
                        sendText(httpExchange, response);
                    }
                    break;
                case "DELETE":
                    response = deleteMethod(httpExchange);
                    if (response.equals("200")) {
                        sendText(httpExchange, response);
                    } else {
                        sendText(httpExchange, response);
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private String getMethod(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String response = null;
        try {
            if (path.equals("/subtasks")) {
                List<Subtask> subtasks = taskManager.getAllSubtasks();
                response = gson.toJson(subtasks);
            } else if (path.matches("/subtasks/\\d+")) {
                int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                Subtask subtask = taskManager.getSubTaskById(id);
                if (subtask != null) {
                    response = gson.toJson(subtask);
                }
            }
        } catch (Exception ignored) {
        }
        return response;
    }

    private String postMethod(HttpExchange httpExchange) throws Exception {
        String path = httpExchange.getRequestURI().getPath();
        String response = "201";
        if (path.equals("/subtasks")) {
            // POST /subtasks - создать новую подзадачу
            String body = readText(httpExchange);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            // Проверка на пересечение с другими задачами
            if (subtask.getStartTime() != null && taskManager.isTaskIntersectsWithOthers(subtask)) {
                return "406"; // Подзадача пересекается с другими
            }
            taskManager.addNewSubtask(subtask);
        } else if (path.matches("/subtasks/\\d+")) {
            // POST /subtasks/{id} - обновить подзадачу
            String body = readText(httpExchange);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            // Проверка на пересечение с другими задачами
            if (subtask.getStartTime() != null && taskManager.isTaskIntersectsWithOthers(subtask)) {
                return "406"; // Подзадача пересекается с другими
            }

            taskManager.updateSubtask(subtask);
        } else {
            response = "400"; // Bad Request - неверный путь
        }
        return response;
    }

    private String deleteMethod(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String response = "200";
        try {
            if (path.equals("/subtasks")) {
                taskManager.deleteAllSubtasks();
            } else if (path.matches("/subtasks/\\d+")) {
                int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                taskManager.deleteSubtaskById(id);
            }
        } catch (Exception exception) {
            response = "404";
        }
        return response;
    }
}