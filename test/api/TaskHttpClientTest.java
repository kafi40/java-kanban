package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.TasksHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.*;
import task.Task;
import util.Managers;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Parameters.DTF;

public class TaskHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getManagerBacked();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
    }
    @Test
    public void shouldGetStatus200ForGetTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus200ForGetTaskId() throws IOException, InterruptedException {
        Task task = new Task("shouldGetStatus200ForGetTaskId", "shouldGetStatus200ForGetTaskId", TaskStatus.NEW);
        taskManager.addTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/1000");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForDeleteTaskId() throws IOException, InterruptedException {
        Task task = new Task("shouldGetStatus200ForDeleteTaskId", "shouldGetStatus200ForDeleteTaskId", TaskStatus.NEW);
        taskManager.addTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus201ForAddTaskId() throws IOException, InterruptedException {
        String result = "{\"taskName\":\"shouldGetStatus201ForAddTaskId\",\"taskDescription\":\"shouldGetStatus201ForAddTaskId\"," +
                        "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"11.06.2024 22:00\"}";
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(201, response.statusCode(), "Ожидался код 201");

    }

    @Test
    public void shouldGetStatus406ForAddTaskId() throws IOException, InterruptedException {
        Task task = new Task("shouldGetStatus406ForAddTaskId", "shouldGetStatus406ForAddTaskId", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.parse("05.06.2024 23:00", DTF));
        task.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task);
        String result = "{\"taskName\":\"Try shouldGetStatus406ForAddTaskId\",\"taskDescription\":\"Try shouldGetStatus406ForAddTaskId\"," +
                        "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"05.06.2024 23:00\"}";
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(406, response.statusCode(), "Ожидался код 406");

    }

    @Test
    public void shouldGetStatus201ForUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("shouldGetStatus201ForUpdateTask", "shouldGetStatus201ForUpdateTask", TaskStatus.NEW);
        taskManager.addTask(task);
        String result = "{\"taskId\":" + task.getTaskId() +",\"taskName\":\"update shouldGetStatus201ForUpdateTask\"," +
                "\"taskDescription\":\"update shouldGetStatus201ForUpdateTask\"," +
                "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"05.06.2024 01:00\"}";
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(201, response.statusCode(), "Ожидался код 201");

    }
    @AfterEach
    public void afterEach() {
        httpServer.stop(0);
    }
}
