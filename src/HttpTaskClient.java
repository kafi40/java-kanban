import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String result = "{\"taskName\":\"Игра на гитаре\",\"taskDescription\":\"Выучить новые аккорды\"," +
                        "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"05.06.2024 17:00\"}";
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(httpRequest, handler);
        System.out.println(response);

        result = "{\"taskName\":\"Тренировка в бассейне\",\"taskDescription\":\"Проплыть 500 метров\"," +
                 "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"05.06.2024 19:00\"}";
        uri = URI.create("http://localhost:8080/tasks");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        System.out.println(response);

        result = "{\"taskName\":\"Свадьба\",\"taskDescription\":\"Подготовка к свадьбе\"}";
        uri = URI.create("http://localhost:8080/epics");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        System.out.println(response);

        result = "{\"taskName\":\"Найти Тамаду\",\"taskDescription\":\"Бюджет 50 000 и без глупых конкурсов\"," +
                 "\"taskStatus\":\"NEW\",\"epicTaskId\":3,\"duration\":60,\"startTime\":\"05.06.2024 10:00\"}";
        uri = URI.create("http://localhost:8080/subtasks");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        System.out.println(response);

        result = "{\"taskId\":1,\"taskName\":\"Игра на гитаре\",\"taskDescription\":\"Повторить ноты\"," +
                 "\"taskStatus\":\"NEW\",\"duration\":60,\"startTime\":\"05.06.2024 17:00\"}";
        uri = URI.create("http://localhost:8080/tasks");
        httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = httpClient.send(httpRequest, handler);
        System.out.println(response);
    }
}
