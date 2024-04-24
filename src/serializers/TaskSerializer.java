package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.Task;

import java.lang.reflect.Type;
import java.util.Optional;

import static util.Parameters.DTF;

public class TaskSerializer implements JsonSerializer<Task> {

    @Override
    public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        long duration = Optional.ofNullable(task.getDuration()).isPresent() ? task.getDuration().toMinutes() : 0;
        String startTime = Optional.ofNullable(task.getStartTime()).isPresent() ?
                task.getStartTime().format(DTF) : "";
        result.addProperty("taskID", task.getTaskId());
        result.addProperty("taskName", task.getTaskName());
        result.addProperty("taskDescription", task.getTaskDescription());
        result.addProperty("taskStatus", String.valueOf(task.getTaskStatus()));
        result.addProperty("duration", duration);
        result.addProperty("startTime", startTime);
        return result;
    }
}
