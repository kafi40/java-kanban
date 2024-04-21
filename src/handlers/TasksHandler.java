package handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TimeIntersectionException;
import task.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static util.Parameters.ENCODING;

public class TasksHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");

        method = exchange.getRequestMethod();
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");
        if (pathSplit.length >= 3) {
            id = Integer.parseInt(pathSplit[2]);
        }

        if (method.equals("GET") && path.equals("/tasks")) {
            response = gson.toJson(taskManager.getTasks().values());
            responseCode = 200;
        }

        if (method.equals("GET") && path.equals("/tasks/" + id)) {
            try {
                response = gson.toJson(taskManager.getTask(id));
                responseCode = 200;
            } catch (RuntimeException e) {
                response = "Задачи не существует!";
                responseCode = 404;
            }
        }

        if (method.equals("DELETE") && path.equals("/tasks/" + id)) {
            taskManager.deleteTask(id);
            response = "Задача удалена";
            responseCode = 200;
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String jsonString = new String(inputStream.readAllBytes(), ENCODING);
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                Task task = gson.fromJson(jsonString, Task.class);
                if (taskManager.isTimeIntersection(task)) {
                    response = "Задачи пересекаются";
                    responseCode = 406;
                    throw new TimeIntersectionException("Задачи пересекаются");
                }
                if (jsonObject.has("taskId")) {
                    taskManager.updateTask(task);
                    response = "Задача отредактирована";
                } else {
                    taskManager.addTask(task);
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
