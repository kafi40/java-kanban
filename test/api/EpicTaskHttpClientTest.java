package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.EpicTaskHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import util.Managers;

import java.io.File;
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

public class EpicTaskHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;
    public static EpicTask epicTask;
    public static EpicTask epicTask1;
    public static SubTask subTask;
    public static File tempFIle;
    @BeforeAll
    public static void beforeAll() throws IOException {
        tempFIle = File.createTempFile("backupTest", "txt", new File("src/resource"));
        Managers.setFileBackedTaskManager(tempFIle);
        taskManager = Managers.getManagerBacked();
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/epics", new EpicTaskHandler());
        httpServer.start();
        epicTask = new EpicTask("Name", "Description");
        epicTask1 = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        taskManager.addEpicTask(epicTask1);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        epicTask.addSubTask(subTask);
    }

    @Test
    public void shouldGetStatus200ForGetEpicTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus200ForGetEpicTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldNotGetStatus200ForGetEpicTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1000");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForGetEpicTasksSubTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldNotGetStatus200ForGetEpicTasksSubTasks()throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1000/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForDeleteEpicTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/2");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus201ForAddEpicTaskId() throws IOException, InterruptedException {
        String result = """
                {"taskName":"Свадьба","taskDescription":"Подготовка к свадьбе"}""";
        URI uri = URI.create("http://localhost:8080/epics");
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
    public void shouldGetStatus201ForUpdateEpicTask() throws IOException, InterruptedException {
        String result = """
                {"taskID":2,"taskName":"Свадьба","taskDescription":"Подготовка к свадьбе"}""";
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(201, response.statusCode(), "Ожидался код 201");

    }
    @AfterAll
    public static void afterAll() {
        tempFIle.deleteOnExit();
    }
}
