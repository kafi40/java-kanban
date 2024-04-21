package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.EpicTask;
import java.lang.reflect.Type;
import java.util.Optional;

import static util.Parameters.DTF;

public class EpicTaskSerializer implements JsonSerializer<EpicTask> {
    @Override
    public JsonElement serialize(EpicTask epicTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        long duration = Optional.ofNullable(epicTask.getDuration()).isPresent() ? epicTask.getDuration().toMinutes() : 0;
        String startTime = Optional.ofNullable(epicTask.getStartTime()).isPresent() ?
                epicTask.getStartTime().format(DTF) : "";
        result.addProperty("taskID", epicTask.getTaskId());
        result.addProperty("taskName", epicTask.getTaskName());
        result.addProperty("taskDescription", epicTask.getTaskDescription());
        result.addProperty("taskStatus", String.valueOf(epicTask.getTaskStatus()));
        result.addProperty("duration", duration);
        result.addProperty("startTime", startTime);
        return result;
    }
}
