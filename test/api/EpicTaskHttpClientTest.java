package api;

import com.sun.net.httpserver.HttpServer;
import handlers.EpicTaskHandler;
import handlers.SubTaskHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Managers;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EpicTaskHttpClientTest {
    public static HttpClient httpClient;
    public static HttpServer httpServer;
    public static File tempFIle;
    public static URI uri;
    public static String string;
    public static String result;
    public static HttpRequest httpRequest;
    public static HttpResponse.BodyHandler<String> handler;
    public static HttpResponse<String> response;
    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        tempFIle = File.createTempFile("backupTest", "txt", new File("src/resource"));
        Managers.setFileBackedTaskManager(tempFIle);
        httpClient = HttpClient.newHttpClient();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/epics", new EpicTaskHandler());
        httpServer.createContext("/subtasks", new SubTaskHandler());
        httpServer.start();

        result = """
                {"taskName":"EpicTask Name","taskDescription":"EpicTask Description"}""";
        uri = URI.create("http://localhost:8080/epics");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        httpClient.send(httpRequest, handler);

        result = """
                {"taskName":"EpicTask Name","taskDescription":"EpicTask Description"}""";
        uri = URI.create("http://localhost:8080/epics");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        httpClient.send(httpRequest, handler);

        result = """
                {"taskName":"SubTask name","taskDescription":"SubTask Description","taskStatus":"NEW","epicTaskId":1,"duration":60,"startTime":"05.06.2024 10:00"}""";
        uri = URI.create("http://localhost:8080/subtasks");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        httpClient.send(httpRequest, handler);

    }

    @Test
    public void shouldGetStatus200ForGetEpicTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus200ForGetEpicTaskId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetEpicTaskId() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/epics/1000");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForGetEpicTasksSubTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus404ForGetEpicTasksSubTasks()throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/epics/1000/subtasks");
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(404, response.statusCode(), "Ожидался код 404");
    }

    @Test
    public void shouldGetStatus200ForDeleteEpicTaskId() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/epics/2");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        assertEquals(200, response.statusCode(), "Ожидался код 200");
    }

    @Test
    public void shouldGetStatus201ForAddEpicTaskId() throws IOException, InterruptedException {
        String result = """
                {"taskName":"EpicTask name","taskDescription":"EpicTask Description"}""";
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
                {"taskId":4,"taskName":"new name","taskDescription":"new description"}""";
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
        httpServer.stop(0);
    }
}
