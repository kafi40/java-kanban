package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.TasksHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
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
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Parameters.DTF;

public class PrioritizedHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getManagerBacked();
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
    }
    @Test
    public void shouldGetStatus200ForGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.parse("15.09.2024 12:00", DTF));
        task.setDuration(Duration.ofMinutes(30));
        Task task1 = new Task("Name", "Description", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.parse("15.09.2024 14:00", DTF));
        task1.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.addTask(task1);
        URI uri = URI.create("http://localhost:8080/priorized");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @AfterEach
    public void afterEach() {
        httpServer.stop(0);
    }
}
