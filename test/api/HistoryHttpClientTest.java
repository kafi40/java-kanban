package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.HistoryHandler;
import handlers.TasksHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;

    @BeforeAll
    public static void beforeAll(){
        taskManager = Managers.getManagerBacked();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.start();
    }
    @Test
    public void shouldGetStatus200ForGetHistory() throws IOException, InterruptedException {
        Task task = new Task("shouldGetStatus200ForGetHistory", "shouldGetStatus200ForGetHistory", TaskStatus.NEW);
        taskManager.addTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getTaskId());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        httpClient.send(httpRequest, handler);

        uri = URI.create("http://localhost:8080/history");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String>  response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @AfterEach
    public void afterEach() {
        httpServer.stop(0);
    }
}
