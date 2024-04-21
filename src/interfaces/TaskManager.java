package interfaces;

import exceptions.TimeIntersectionException;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, EpicTask>  getEpicTasks();

    HashMap<Integer, SubTask>  getSubTasks();

    List<SubTask> getEpicSubTasks(int epicId);

    void deleteTasks();

    void deleteEpicTasks();

    void deleteSubTasks();

    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    void addTask(Task task);

    void addEpicTask(EpicTask epicTask);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    void deleteTask(int id);

    void deleteEpicTask(int id);

    void deleteSubTask(int id);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

    boolean isTimeIntersection(Task task) throws TimeIntersectionException;

}