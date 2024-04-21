package deserializers;

import com.google.gson.*;
import enums.TaskStatus;
import task.Task;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.Parameters.DTF;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        TaskStatus taskStatus = switch (jsonObject.get("taskStatus").getAsString()) {
            case "NEW" -> TaskStatus.NEW;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "DONE" -> TaskStatus.DONE;
            default -> null;
        };
        Task task = new Task(
                jsonObject.get("taskName").getAsString(),
                jsonObject.get("taskDescription").getAsString(),
                taskStatus
        );
        if (jsonObject.has("taskId")) {
            task.setTaskId(jsonObject.get("taskId").getAsInt());
        }
        task.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DTF));
        task.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));

        return task;
    }
}
