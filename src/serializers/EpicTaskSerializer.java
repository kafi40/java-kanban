package serializers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.EpicTask;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import static util.Parameters.DTF;

public class EpicTaskSerializer implements JsonSerializer<EpicTask> {
    @Override
    public JsonElement serialize(EpicTask epicTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("taskID", epicTask.getTaskId());
        result.addProperty("taskName", epicTask.getTaskName());
        result.addProperty("taskDescription", epicTask.getTaskDescription());
        result.addProperty("taskStatus", String.valueOf(epicTask.getTaskStatus()));
        result.addProperty("duration", epicTask.getDuration().toMinutes());
        result.addProperty("startTime", epicTask.getStartTime().format(DTF));
        return result;
    }
}
