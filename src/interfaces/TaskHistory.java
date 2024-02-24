package interfaces;

import task.Task;

import java.util.ArrayList;

public interface TaskHistory {

    void addTaskInHistory(Task task);

    ArrayList<Task> getHistory();

}
