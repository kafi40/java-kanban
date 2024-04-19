import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.TaskStatus;
import serializers.EpicTaskSerializer;
import serializers.SubTaskSerializer;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.Parameters.DTF;

public class HttpTaskClient {
    public final HttpClient httpClient = HttpClient.newHttpClient();
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskClient httpTaskClient = new HttpTaskClient();
        Gson gson =  new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(EpicTask.class, new EpicTaskSerializer())
                .registerTypeAdapter(SubTask.class, new SubTaskSerializer())
                .create();;
        String result;
        Task createTask = new Task("Игра на гитаре", "Выучить новые аккорды", TaskStatus.NEW);
        createTask.setStartTime(LocalDateTime.parse("05.06.2024 18:00", DTF));
        createTask.setDuration(Duration.ofMinutes(60));

        result = gson.toJson(createTask);

        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(result))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpTaskClient.httpClient.send(httpRequest, handler);
        System.out.println(response.statusCode());
    }
}
