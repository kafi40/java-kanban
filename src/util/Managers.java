package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import io.FileBackedTaskManager;
import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import static util.Parameters.FILE;


public class Managers {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static final InMemoryHistoryManager inMemoryHistoryManager  = new InMemoryHistoryManager();
    private static FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(FILE);

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getManagerBacked() {
        return fileBackedTaskManager;
    }

}
