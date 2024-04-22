package handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.EpicTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static util.Parameters.ENCODING;

public class EpicTaskHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /epics запроса от клиента.");

        method = exchange.getRequestMethod();
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");
        if (pathSplit.length >= 3) {
            id = Integer.parseInt(pathSplit[2]);
        }

        if (method.equals("GET") && path.equals("/epics")) {
            response = gson.toJson(taskManager.getEpicTasks().values());
            responseCode = 200;
        }

        if (method.equals("GET") && path.equals("/epics/" + id)) {
            if (taskManager.getEpicTask(id) != null) {
                response = gson.toJson(taskManager.getEpicTask(id));
                responseCode = 200;
            } else {
                response = "Задачи не существует!";
                responseCode = 404;
            }
        }

        if (method.equals("GET") && path.equals("/epics/" + id + "/subtasks")) {
            if (taskManager.getEpicSubTasks(id) != null) {
                response = gson.toJson(taskManager.getEpicSubTasks(id));
                responseCode = 200;
            } else {
                response = "Задачи не существует!";
                responseCode = 404;
            }
        }

        if (method.equals("DELETE") && path.equals("/epics/" + id)) {
            taskManager.deleteEpicTask(id);
            response = "Задача удалена";
            responseCode = 200;
        }

        if (method.equals("POST") && path.equals("/epics")) {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String jsonString = new String(inputStream.readAllBytes(), ENCODING);
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                EpicTask epicTask = gson.fromJson(jsonString, EpicTask.class);
                if (jsonObject.has("taskId")) {
                    taskManager.updateEpicTask(epicTask);
                } else {
                    taskManager.addEpicTask(epicTask);
                }
            }
            response = "Задача добавлена";
            responseCode = 201;
        }

        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
