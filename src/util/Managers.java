package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import io.FileBackedTaskManager;
import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import java.io.File;


public class Managers {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static final InMemoryHistoryManager inMemoryHistoryManager  = new InMemoryHistoryManager();
    private static FileBackedTaskManager fileBackedTaskManager;

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getManagerBacked() {
        return fileBackedTaskManager;
    }

    public static void setFileBackedTaskManager(File file) {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
    }
}
