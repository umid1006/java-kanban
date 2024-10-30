import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import manager.UserManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpUserServer;
import user.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpUserServerTest {

    private HttpUserServer userServer;
    private final Gson gson = Managers.getGson();

    private TaskManager taskManager;
    private UserManager userManager;

    private Task task;
    private User user;

    @BeforeEach
    void init() throws IOException {
        userManager = Managers.getUserDefault();
        taskManager = userManager.getTaskmanager();

        userServer = new HttpUserServer(userManager);

        user = new User("Тестов Тест Тестович");
        userManager.add(user);

        task = new Task(45, "Test task", "Task task description",
                NEW, 15, LocalDateTime.now(), user);
        taskManager.addNewTask(task);

        userServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        userServer.stop();
    }

    @Test
    void getUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type userType = new TypeToken<ArrayList<User>>() {
        }.getType();
        List<User> actual = gson.fromJson(response.body(), userType);

        assertNotNull(actual, "Пользователи не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество пользователей");
        assertEquals(user, actual.getFirst(), "Пользователи не совподают");

    }

    @Test
    void getUserById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type userType = new TypeToken<User>() {
        }.getType();
        User actual = gson.fromJson(response.body(), userType);

        assertNotNull(actual, "Пользователи не возвращаются");
        assertEquals(user, actual, "Пользователи не совподают");
    }

    @Test
    void getUserTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/1/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество задач");
        assertEquals(task, actual.getFirst(), "Задачи не совподают");
    }

    @Test
    void deleteUser() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

}
