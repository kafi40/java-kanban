package interfaces;

import task.Task;
import java.util.Set;

public interface TaskHistory {

    void addTaskInHistory(Task task);

    Set<Task> getHistory();

    void removeTaskFromHistory(int id);

}
