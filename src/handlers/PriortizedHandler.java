package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class PriortizedHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /prioritized запроса от клиента.");

        method = exchange.getRequestMethod();
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");
        id = Integer.parseInt(pathSplit[2]);

        if (method.equals("GET") && path.equals("/prioritized")) {
            response = taskManager.getPrioritizedTasks().toString();
        }

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
