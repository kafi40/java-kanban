package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import task.SubTask;

import java.lang.reflect.Type;

import static util.Parameters.DTF;

public class SubTaskSerializer implements JsonSerializer<SubTask> {
    @Override
    public JsonElement serialize(SubTask subTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("taskID", subTask.getTaskId());
        result.addProperty("taskName", subTask.getTaskName());
        result.addProperty("taskDescription", subTask.getTaskDescription());
        result.addProperty("taskStatus", String.valueOf(subTask.getTaskStatus()));
        result.addProperty("epicTaskName", subTask.getEpicTask().getTaskName());
        result.addProperty("duration", subTask.getDuration().toMinutes());
        result.addProperty("startTime", subTask.getStartTime().format(DTF));
        return result;
    }
}
