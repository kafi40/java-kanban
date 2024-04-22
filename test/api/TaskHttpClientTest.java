package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.TasksHandler;
import interfaces.TaskManager;
import memory.InMemoryTaskManager;
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
    public static Task task;
    public static Task task1;

    @BeforeAll
    public static void beforeAll() {
        taskManager = null;
        taskManager = Managers.getDefault();
        task = new Task("Name", "Description", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        task.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task);
        task1 = new Task("Name", "Description", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        task1.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task1);
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
        URI uri = URI.create("http://localhost:8080/tasks/" + task1.getTaskId());
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
        String result = """
                {"taskName":"Игра на гитаре","taskDescription":"Выучить новые аккорды","taskStatus":"NEW","duration":60,"startTime":"05.06.2024 23:00"}""";
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
        String result = """
                {"taskName":"Игра на гитаре","taskDescription":"Выучить новые аккорды","taskStatus":"NEW","duration":60,"startTime":"05.06.2024 23:00"}""";
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
        String result = """
                {"taskId":1,"taskName":"new name","taskDescription":"new description","taskStatus":"NEW","duration":60,"startTime":"05.06.2024 01:00"}""";
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

    @AfterAll
    public static void afterAll() {
        InMemoryTaskManager.taskIdCounter = 0;
    }
}
