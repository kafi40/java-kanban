package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.EpicTaskHandler;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTaskHttpClientTest {
    public  static HttpClient httpClient;
    public  static HttpServer httpServer;
    public  static TaskManager taskManager;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getManagerBacked();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/epics", new EpicTaskHandler());
        httpServer.start();
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
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus200ForGetEpicTaskId", "shouldGetStatus200ForGetEpicTaskId");
        taskManager.addEpicTask(epicTask);
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
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus200ForGetEpicTasksSubTasks", "Epic shouldGetStatus200ForGetEpicTasksSubTasks");
        taskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Sub shouldGetStatus200ForGetEpicTasksSubTasks", "Sub shouldGetStatus200ForGetEpicTasksSubTasks", TaskStatus.NEW, epicTask.getTaskId());
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
        EpicTask epicTask = new EpicTask("Epic shouldGetStatus200ForDeleteEpicTaskId", "Epic shouldGetStatus200ForDeleteEpicTaskId");
        taskManager.addEpicTask(epicTask);
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
                {"taskName":"shouldGetStatus201ForAddEpicTaskId","taskDescription":"shouldGetStatus201ForAddEpicTaskId"}""";
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
        EpicTask epicTask = new EpicTask("shouldGetStatus201ForUpdateEpicTask", "shouldGetStatus201ForUpdateEpicTask");
        taskManager.addEpicTask(epicTask);
        String result = "{\"taskId\":\"" + epicTask.getTaskId() +"\",\"taskName\":\"Update shouldGetStatus201ForUpdateEpicTask\"," +
                        "\"taskDescription\":\"Update shouldGetStatus201ForUpdateEpicTask\"}";
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
        httpServer.stop(0);
    }
}
