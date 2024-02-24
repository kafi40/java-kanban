package memory;

import interfaces.TaskHistory;
import task.Task;

import java.util.ArrayList;

public class InMemoryTaskHistory implements TaskHistory {

    private final ArrayList<Task> tasksHistory = new ArrayList<>(10);

    @Override
    public void addTaskInHistory(Task task) {
        tasksHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return tasksHistory;
    }
}