package util;

import interfaces.TaskManager;
import memory.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
