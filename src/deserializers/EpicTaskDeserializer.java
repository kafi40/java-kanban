package deserializers;

import com.google.gson.*;
import task.EpicTask;
import java.lang.reflect.Type;

public class EpicTaskDeserializer implements JsonDeserializer<EpicTask> {
    @Override
    public EpicTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        EpicTask epicTask = new EpicTask(
                jsonObject.get("taskName").getAsString(),
                jsonObject.get("taskDescription").getAsString()
        );
        if (jsonObject.has("taskId")) {
            epicTask.setTaskId(jsonObject.get("taskId").getAsInt());
        }

        return epicTask;
    }
}
