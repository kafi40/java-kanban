package deserializers;

import com.google.gson.*;
import enums.TaskStatus;
import task.SubTask;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.Parameters.DTF;

public class SubTaskDeserializer implements JsonDeserializer<SubTask> {
    @Override
    public SubTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        TaskStatus taskStatus = switch (jsonObject.get("taskStatus").getAsString()) {
            case "NEW" -> TaskStatus.NEW;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "DONE" -> TaskStatus.DONE;
            default -> null;
        };
        SubTask subTask = new SubTask(
                jsonObject.get("taskName").getAsString(),
                jsonObject.get("taskDescription").getAsString(),
                taskStatus,
                jsonObject.get("epicTaskId").getAsInt()
        );
        if (jsonObject.has("taskId")) {
            subTask.setTaskId(jsonObject.get("taskId").getAsInt());
        }
        if (!jsonObject.get("startTime").getAsString().isEmpty() && jsonObject.get("duration").getAsInt() != 0) {
            subTask.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DTF));
            subTask.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
        }
        return subTask;
    }
}
