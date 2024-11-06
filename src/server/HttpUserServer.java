package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import manager.UserManager;
import server.handler.EpicHandler;
import server.handler.PrioritizedHandler;
import server.handler.SubtaskHandler;
import server.handler.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUserServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;

    private TaskManager taskManager;
    private final UserManager userManager;

    public HttpUserServer() throws IOException {
        this(Managers.getUserDefault());
    }

    public HttpUserServer(UserManager userManager) throws IOException {
        this.userManager = userManager;
        this.taskManager = userManager.getTaskmanager();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("LocalHost", PORT), 0);

        server.createContext("/api/v1/users", this::handleUsers);
        server.createContext("/tasks", (httpExchange) -> {
            new TaskHandler(taskManager, gson).handle(httpExchange);
        }); // Add context for /tasks
        server.createContext("/subtasks", (httpExchange) -> {
            new SubtaskHandler(taskManager, gson).handle(httpExchange);
        });
        server.createContext("/epics", (httpExchange) -> {
            new EpicHandler(taskManager, gson).handle(httpExchange);
        });
        server.createContext("/prioritized", (httpExchange) -> {
            new PrioritizedHandler(taskManager, gson).handle(httpExchange);
        });
    }

    private void handleUsers(HttpExchange httpExchange) {

        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/users$", path)) {
                        String responce = gson.toJson(userManager.getAll());
                        sendText(httpExchange, responce);
                        return;
                    }
                    if (Pattern.matches("^/api/v1/users/\\d+$", path)) {
                        String pathId = path.replaceFirst("^/api/v1/users/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String responce = gson.toJson(userManager.getById(id));
                            sendText(httpExchange, responce);
                            break;
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/api/v1/users/\\d+/tasks$", path)) {
                        String pathId = path.replaceFirst("^/api/v1/users/", "")
                                .replaceFirst("/tasks", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(userManager.getUserTask(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/api/v1/users/\\d+$", path)) {
                        String pathId = path.replaceFirst("^/api/v1/users/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            userManager.delete(id);
                            System.out.println("Удалили пользователя id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Ждем GET или DELETE запрос, а получили - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Started UserServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/api/v1/users");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Context-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        final HttpUserServer server = new HttpUserServer();
        server.start();
    }
}