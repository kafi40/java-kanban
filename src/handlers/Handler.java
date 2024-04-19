package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import serializers.EpicTaskSerializer;
import serializers.SubTaskSerializer;
import task.EpicTask;
import task.SubTask;
import tokens.TaskListTypeToken;
import util.Managers;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Handler implements HttpHandler {
    public static TaskManager taskManager = Managers.getManagerBacked();
    public static TaskListTypeToken token = new TaskListTypeToken();
    public Gson gson;
    public String method;
    public String path;
    public String request;
    public String response;
    public int responseCode;
    public String[] pathSplit;
    public int id;

    public Handler() {
        gson = new GsonBuilder().setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(EpicTask.class, new EpicTaskSerializer())
                .registerTypeAdapter(SubTask.class, new SubTaskSerializer())
                .create();
    }
}
