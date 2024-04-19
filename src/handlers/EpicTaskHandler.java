package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

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
            response = gson.toJson(taskManager.getEpicTask(id));
            if (!response.equals("null")) {
                responseCode = 200;
            } else {
                response = "Задачи не существует!";
                responseCode = 404;
            }
        }

        if (method.equals("GET") && path.equals("/epics/" + id + "/subtasks")) {
            response = gson.toJson(taskManager.getEpicTask(id).getSubTasks());
            if (!response.equals("null")) {
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
//            response = taskManager.addTask();
        }

        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
