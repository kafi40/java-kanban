package memory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import util.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    public static InMemoryTaskManager taskManager;
    static public Task task;
    static public EpicTask epicTask;
    static public SubTask subTask;

    @BeforeAll
    public static void beforeAll() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        task = new Task("Name", "Description", TaskStatus.NEW);
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
    }

    @Test
    public void canGetTaskManagerFromManagers() {
        assertNotNull(taskManager, "Менеджер задач не создался");
    }

    @Test
    public void canAddTasksInMaps() {
        int taskId = task.getTaskId();
        taskManager.getAllTypeTasks().put(taskId, task);
        taskManager.getTasks().put(taskId, task);
        int epicTaskId = epicTask.getTaskId();
        taskManager.getAllTypeTasks().put(epicTaskId, task);
        taskManager.getEpicTasks().put(epicTaskId, epicTask);
        int subTaskId = subTask.getTaskId();
        taskManager.getAllTypeTasks().put(subTaskId, task);
        taskManager.getSubTasks().put(subTaskId, subTask);

        assertNotNull(taskManager.getAllTypeTasks().get(taskId), "Обычная задача не добавилась в общий список");
        assertNotNull(taskManager.getAllTypeTasks().get(epicTaskId), "Эпик задача не добавилась в общий список");
        assertNotNull(taskManager.getAllTypeTasks().get(subTaskId), "Подзадача не добавилась в общий список");

        assertNotNull(taskManager.getTasks().get(taskId), "Обычная задача не добавилась список обычных задач");
        assertNotNull(taskManager.getEpicTasks().get(epicTaskId), "Эпик задача не добавилась список эпик задач");
        assertNotNull(taskManager.getSubTasks().get(subTaskId), "Подзадача не добавилась список подзадач");
    }
}
