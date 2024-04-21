package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.SubTask;

import java.lang.reflect.Type;
import java.util.Optional;

import static util.Parameters.DTF;

public class SubTaskSerializer implements JsonSerializer<SubTask> {
    @Override
    public JsonElement serialize(SubTask subTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        long duration = Optional.ofNullable(subTask.getDuration()).isPresent() ? subTask.getDuration().toMinutes() : 0;
        String startTime = Optional.ofNullable(subTask.getStartTime()).isPresent() ?
                subTask.getStartTime().format(DTF) : "";
        result.addProperty("taskID", subTask.getTaskId());
        result.addProperty("taskName", subTask.getTaskName());
        result.addProperty("taskDescription", subTask.getTaskDescription());
        result.addProperty("taskStatus", String.valueOf(subTask.getTaskStatus()));
        result.addProperty("epicTaskId", subTask.getEpicTaskId());
        result.addProperty("duration", duration);
        result.addProperty("startTime", startTime);
        return result;
    }
}
