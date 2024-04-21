import com.sun.net.httpserver.HttpServer;
import handlers.*;
import util.Managers;
import java.io.IOException;
import java.net.InetSocketAddress;
import static util.Parameters.FILE;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer = HttpServer.create();

    public HttpTaskServer() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
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
}
