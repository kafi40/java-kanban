import com.sun.net.httpserver.HttpServer;
import handlers.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer = HttpServer.create();

    public HttpTaskServer() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        while (true) {
            System.out.println("0 - Остановить сервер");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                int exit = scanner.nextInt();
                if (exit == 0) {
                    httpTaskServer.stop();
                    break;
                }
            }
        }
    }

    public void start() {
        try {
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/epics", new EpicTaskHandler());
            httpServer.createContext("/subtasks", new SubTaskHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PriortizedHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}
