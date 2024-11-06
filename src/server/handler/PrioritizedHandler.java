package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {  // No need to throw IOException
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String response;

            if (method.equals("GET") && path.equals("/prioritized")) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                response = gson.toJson(prioritizedTasks);
                sendText(httpExchange, response); // Assuming sendText takes 3 arguments
            } else {
                httpExchange.sendResponseHeaders(400, -1);
            }
        } catch (Exception e) {
            // ... error handling (log the exception) ...
        } finally {
            httpExchange.close();
        }
    }
}