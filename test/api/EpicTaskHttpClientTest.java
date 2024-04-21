package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.EpicTaskHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.*;
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
    public  HttpClient httpClient;
    public  HttpServer httpServer;
    public static File tempFIle;
    public TaskManager taskManager;
    public EpicTask epicTask;
    public SubTask subTask;
    @BeforeAll
    public static void beforeAll() throws IOException {
        tempFIle = File.createTempFile("backupTest", "txt", new File("src/resource"));
        Managers.setFileBackedTaskManager(tempFIle);
    }
    @BeforeEach
    public void beforeEach() throws IOException {
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/epics", new EpicTaskHandler());
        httpServer.start();
        taskManager = Managers.getManagerBacked();
    }

    @Test
    public void shouldGetStatus200ForGetEpicTasks() throws IOException, InterruptedException {
        epicTask = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
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
        epicTask = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/epics/" + epicTask.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetEpicTaskId() throws IOException, InterruptedException {
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
        epicTask = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/epics/" + epicTask.getTaskId() + "/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetEpicTasksSubTasks()throws IOException, InterruptedException {
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
        epicTask = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/epics/" + epicTask.getTaskId());
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
                {"taskId":1,"taskName":"new name","taskDescription":"new description"}""";
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
    @AfterEach
    public void afterEach() {
        tempFIle.deleteOnExit();
        httpServer.stop(0);
    }
}
