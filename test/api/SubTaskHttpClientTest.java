package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.SubTaskHandler;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static util.Parameters.DTF;

public class SubTaskHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;
    public static EpicTask epicTask;
    public static SubTask subTask;
    public static SubTask subTask1;
    public static File tempFIle;
    @BeforeAll
    public static void beforeAll() throws IOException {
        tempFIle = File.createTempFile("backupTest", "txt", new File("src/resource"));
        Managers.setFileBackedTaskManager(tempFIle);
        taskManager = Managers.getManagerBacked();
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/subtasks", new SubTaskHandler());
        httpServer.start();
        epicTask = new EpicTask("Name", "Description");
        taskManager.addEpicTask(epicTask);
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        subTask1 = new SubTask("Name", "Description", TaskStatus.NEW, 1);
        subTask1.setStartTime(LocalDateTime.parse("15.04.2024 13:00", DTF));
        subTask1.setDuration(Duration.ofMinutes(30));
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask1);
    }

    @Test
    public void shouldGetStatus200ForGetSubTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus200ForGetSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldNotGetStatus200ForGetSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/1000");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertNotEquals(200, response.statusCode(), "Ожидался код отличный от 200");
    }

    @Test
    public void shouldGetStatus200ForDeleteSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus201ForAddSubTaskId() throws IOException, InterruptedException {
        String result = """
                {"taskName":"Найти Тамаду","taskDescription":"Бюджет 50 000 и без глупых конкурсов","taskStatus":"NEW","epicTaskId":1,"duration":60,"startTime":"05.06.2024 10:00"}""";
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
    public void shouldGetStatus406ForAddTaskId() throws IOException, InterruptedException {
        String result = """
                {"taskName":"Найти Тамаду","taskDescription":"Бюджет 50 000 и без глупых конкурсов","taskStatus":"NEW","epicTaskId":1,"duration":60,"startTime":"05.06.2024 10:00"}""";
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
        String result = """
                {"taskId":4, "taskName":"Найти Тамаду","taskDescription":"Бюджет 50 000","taskStatus":"NEW","epicTaskId":1,"duration":60,"startTime":"05.06.2024 10:00"}""";
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
    @AfterAll
    public static void afterAll() {
        tempFIle.deleteOnExit();
        httpServer.stop(0);
    }
}
