package api;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import handlers.HistoryHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Task;
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

public class HistoryHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;
    public static Task task;
    public static File tempFIle;
    @BeforeAll
    public static void beforeAll() throws IOException {
        tempFIle = File.createTempFile("backupTest", "txt", new File("src/resource"));
        Managers.setFileBackedTaskManager(tempFIle);
        taskManager = Managers.getManagerBacked();
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.start();
        task = new Task("Name", "Description", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        task.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.getTask(1);
    }

    @Test
    public void shouldGetStatus200ForGetHistory() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }
    @AfterAll
    public static void afterAll() {
        tempFIle.deleteOnExit();
        httpServer.stop(0);
    }
}
