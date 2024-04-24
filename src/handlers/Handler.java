package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import deserializers.EpicTaskDeserializer;
import deserializers.SubTaskDeserializer;
import deserializers.TaskDeserializer;
import interfaces.TaskManager;
import serializers.EpicTaskSerializer;
import serializers.SubTaskSerializer;
import serializers.TaskSerializer;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Handler implements HttpHandler {
    public static TaskManager taskManager = Managers.getManagerBacked();
    public Gson gson;
    public String method;
    public String path;
    public String response;
    public int responseCode;
    public String[] pathSplit;
    public int id;

    public Handler() {
        gson = new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(EpicTask.class, new EpicTaskSerializer())
                .registerTypeAdapter(SubTask.class, new SubTaskSerializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(EpicTask.class, new EpicTaskDeserializer())
                .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
                .create();
    }
}
