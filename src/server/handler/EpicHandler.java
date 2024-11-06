package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String response = "";
            int responseCode;
            try {
                responseCode = switch (method) {
                    case "GET" -> {
                        response = getMethod(httpExchange, path);
                        yield response != null ? 200 : 404;
                    }
                    case "POST" -> {
                        response = postMethod(httpExchange, path);
                        yield response.equals("201") ? 201 : 400;
                    }
                    case "DELETE" -> {
                        response = deleteMethod(httpExchange, path);
                        yield response.equals("200") ? 200 : 404;
                    }
                    default -> 405;
                };
            } catch (NotFoundException e) {
                responseCode = 404;
                response = e.getMessage();
            }
            assert response != null;
            sendText(httpExchange, response);
        } catch (Exception e) {

            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private String getMethod(HttpExchange httpExchange, String path) throws NotFoundException {
        String response = null;
        int id = -1;
        if (path.equals("/epics")) {
            List<Epic> epics = taskManager.getAllEpics();
            response = gson.toJson(epics);
        } else if (path.matches("/epics/\\d+")) {
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                response = gson.toJson(epic);
            }
        } else if (path.matches("/epics/\\d+/subtasks")) {
            List<Subtask> subtasks = taskManager.getEpicSubtasks(id);
            response = gson.toJson(subtasks);
        }
        return response;
    }

    private String postMethod(HttpExchange httpExchange, String path) throws Exception {
        String response = "201";
        if (path.equals("/epics")) {
            String body = readText(httpExchange);
            Epic epic = gson.fromJson(body, Epic.class);
            taskManager.addNewEpic(epic);
        } else if (path.matches("/epics/\\d+")) {
            String body = readText(httpExchange);
            Epic epic = gson.fromJson(body, Epic.class);
        }
        return response;
    }

    private String deleteMethod(HttpExchange httpExchange, String path) {
        String response = "200";
        if (path.equals("/epics")) {
            taskManager.deleteAllEpics();
        } else if (path.matches("/epics/\\d+")) {
            int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
            taskManager.deleteEpicById(id);
        }
        return response;
    }
}