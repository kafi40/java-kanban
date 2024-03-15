package interfaces;

import task.Task;
import enums.TaskStatus;

import java.io.IOException;

public interface TaskManager {

    TaskStatus setTaskStatus(int command);

    void setEpicTaskStatus();

    void showTask(Task task);

    void addTask(int command) throws IOException;

    void clearTasks(int command);

    void findTaskById(int taskId);

    void deleteTaskById(int taskId);

    void showTasks(int command);

    void editTaskById(int taskId);

    void getHistory();
}