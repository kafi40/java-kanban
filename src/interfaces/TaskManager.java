package interfaces;

import task.Task;
import enums.TaskStatus;

import java.io.IOException;
import java.util.TreeSet;

public interface TaskManager {

    TaskStatus setTaskStatus(int command);

    void setEpicTaskStatus();

    void showTask(Task task);

    void addTask(int command) throws IOException;

    void clearTasks(int command);

    void findTaskById(int taskId) throws IOException;

    void deleteTaskById(int taskId);

    void showTasks(int command);

    void editTaskById(int taskId);

    void getHistory();

    TreeSet<Task> getPrioritizedTasks();
}