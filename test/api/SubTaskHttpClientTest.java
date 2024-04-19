package api;

import com.sun.net.httpserver.HttpServer;
import handlers.SubTaskHandler;
import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SubTaskHttpClientTest {
    public static HttpClient httpClient;
    public static TaskManager taskManager;
    public static HttpServer httpServer;
    @BeforeAll
    public static void beforeAll() throws IOException {
        httpClient = HttpClient.newHttpClient();
        taskManager = Managers.getManagerBacked();
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
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
        System.out.println(response.body());
    }

    @Test
    public void shouldGetStatus200ForGetSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/5");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
        System.out.println(response.body());
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
        System.out.println(response.body());
    }

    @Test
    public void shouldGetStatus200ForDeleteSubTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/8");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }
}
