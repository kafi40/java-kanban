package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
            response = gson.toJson(taskManager.getTask(id));
            if (!response.equals("null")) {
                responseCode = 200;
            } else {
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
                String s = new String(inputStream.readAllBytes(), ENCODING);
                System.out.println(s);
            }
        }

        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
