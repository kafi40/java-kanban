package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.SubTaskHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.*;
import task.EpicTask;
import task.SubTask;
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

public class SubTaskHttpClientTest {
    public static HttpClient httpClient;
    public static HttpServer httpServer;
    public static TaskManager taskManager;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getManagerBacked();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/subtasks", new SubTaskHandler());
        httpServer.start();
    }
    @Test
    public void shouldGetStatus200ForGetSubTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus200ForGetSubTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus200ForGetSubTaskId", "Epic shouldGetStatus200ForGetSubTaskId");
        taskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Sub shouldGetStatus200ForGetSubTaskId", "Sub shouldGetStatus200ForGetSubTaskId", TaskStatus.NEW, epicTask.getTaskId());
        taskManager.addSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/subtasks/" + subTask.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/1000");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForDeleteSubTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus200ForDeleteSubTaskId", "Epic shouldGetStatus200ForDeleteSubTaskId");
        taskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Sub shouldGetStatus200ForDeleteSubTaskId", "Sub shouldGetStatus200ForDeleteSubTaskId", TaskStatus.NEW, epicTask.getTaskId());
        taskManager.addSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/subtasks/" + subTask.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus201ForAddSubTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus201ForAddSubTaskId", "Epic shouldGetStatus201ForAddSubTaskId");
        taskManager.addEpicTask(epicTask);
        String result = "{\"taskName\":\"Sub shouldGetStatus201ForAddSubTaskId\",\"taskDescription\":\"Sub shouldGetStatus201ForAddSubTaskId\"," +
                        "\"taskStatus\":\"NEW\",\"epicTaskId\":\"" + epicTask.getTaskId() + "\",\"duration\":30,\"startTime\":\"01.06.2024 19:00\"}";
        URI uri = URI.create("http://localhost:8080/subtasks");
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
    public void shouldGetStatus406ForAddSubTaskId() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus406ForAddSubTaskId", "Epic shouldGetStatus406ForAddSubTaskId");
        taskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Sub shouldGetStatus406ForAddSubTaskId", "Sub shouldGetStatus406ForAddSubTaskId", TaskStatus.NEW, epicTask.getTaskId());
        subTask.setStartTime(LocalDateTime.parse("01.06.2024 16:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        String result = "{\"taskName\":\"Try Sub shouldGetStatus406ForAddSubTaskId\",\"taskDescription\":\"Try Sub shouldGetStatus406ForAddSubTaskId\"," +
                        "\"taskStatus\":\"NEW\",\"epicTaskId\":\"" + epicTask.getTaskId() + "\",\"duration\":30,\"startTime\":\"01.06.2024 16:00\"}";
        URI uri = URI.create("http://localhost:8080/subtasks");
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
    public void shouldGetStatus201ForUpdateSubTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus201ForUpdateSubTask", "Epic shouldGetStatus201ForUpdateSubTask");
        taskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Sub shouldGetStatus201ForUpdateSubTask", "Sub shouldGetStatus201ForUpdateSubTask", TaskStatus.NEW, epicTask.getTaskId());
        taskManager.addSubTask(subTask);
        String result = "{\"taskId\":\"" + subTask.getTaskId() + "\", \"taskName\":\"Update Sub shouldGetStatus201ForUpdateSubTask\"," +
                        "\"taskDescription\":\"Update Sub shouldGetStatus201ForUpdateSubTask\"," + "\"taskStatus\":\"NEW\"" +
                        ",\"epicTaskId\":\"" + epicTask.getTaskId() + "\",\"duration\":30,\"startTime\":\"06.06.2024 04:00\"}";
        URI uri = URI.create("http://localhost:8080/subtasks");
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
