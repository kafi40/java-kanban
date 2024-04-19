package interfaces;

import task.Task;
import java.util.List;

public interface HistoryManager<T> {
    void add(T task);
    void remove(int id);
    List<Task> getHistory();
}
