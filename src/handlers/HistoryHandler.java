package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class HistoryHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /history запроса от клиента.");

        method = exchange.getRequestMethod();
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");
        if (pathSplit.length >= 3) {
            id = Integer.parseInt(pathSplit[2]);
        }

        if (method.equals("GET") && path.equals("/history")) {
            response = gson.toJson(taskManager.getHistory());
            responseCode = 200;
        }

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
