package interfaces;

import task.Task;
import java.util.List;

public interface TaskHistory<T extends Task> {

    void addTaskInHistory(T element);

    List<Task> getHistory();

    void removeTaskFromHistory(int id);

}
