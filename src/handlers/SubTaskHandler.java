package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

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
            response = gson.toJson(taskManager.getSubTask(id));
            if (!response.equals("null")) {
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
//            response = taskManager.addTask();
        }

        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
