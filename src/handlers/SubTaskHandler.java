package handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TimeIntersectionException;
import task.SubTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static util.Parameters.ENCODING;

public class SubTaskHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /subtasks запроса от клиента.");
        method = exchange.getRequestMethod();
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");

        if (pathSplit.length >= 3) {
            id = Integer.parseInt(pathSplit[2]);
        }

        if (method.equals("GET") && path.equals("/subtasks")) {
            response = gson.toJson(taskManager.getSubTasks());
            responseCode = 200;
        }

        if (method.equals("GET") && path.equals("/subtasks/" + id)) {
            if (taskManager.getSubTask(id) != null) {
                response = gson.toJson(taskManager.getSubTask(id));
                responseCode = 200;
            } else {
                response = "Задачи не существует!";
                responseCode = 404;
            }
        }

        if (method.equals("DELETE") && path.equals("/subtasks/" + id)) {
            taskManager.deleteSubTask(id);
            response = "Задача удалена";
            responseCode = 200;
        }

        if (method.equals("POST") && path.equals("/subtasks")) {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String jsonString = new String(inputStream.readAllBytes(), ENCODING);
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                SubTask subTask = gson.fromJson(jsonString, SubTask.class);
                if (taskManager.isTimeIntersection(subTask)) {
                    response = "Задачи пересекаются";
                    responseCode = 406;
                    throw new TimeIntersectionException("Задачи пересекаются");
                }
                if (jsonObject.has("taskId")) {
                    taskManager.updateSubTask(subTask);
                    response = "Задача отредактирована";
                } else {
                    taskManager.addSubTask(subTask);
                    response = "Задача добавлена";
                }
                responseCode = 201;
            } catch (TimeIntersectionException e) {
                System.out.println(e);
            }
        }
        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
