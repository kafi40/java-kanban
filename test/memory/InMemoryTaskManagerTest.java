package memory;

import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import enums.TaskStatus;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static util.Parameters.DTF;

public class InMemoryTaskManagerTest {
    public static InMemoryTaskManager taskManager;
    @Test
    public void canGetTaskManagerFromManagers() {
        assertNotNull(taskManager, "Менеджер задач не создался");
    }

    @Test
    public void canAddTasksInMaps() {
        taskManager = new InMemoryTaskManager();
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        SubTask subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);
        taskManager.getEpicTasks().put(epicTask.getTaskId(), epicTask);
        taskManager.getSubTasks().put(subTask.getTaskId(), subTask);
        taskManager.getSubTasks().put(subTask2.getTaskId(), subTask2);
        int taskId = task.getTaskId();
        taskManager.getTasks().put(taskId, task);
        int epicTaskId = epicTask.getTaskId();
        taskManager.getEpicTasks().put(epicTaskId, epicTask);
        int subTaskId = subTask.getTaskId();
        taskManager.getSubTasks().put(subTaskId, subTask);

        assertNotNull(taskManager.getTasks().get(taskId), "Обычная задача не добавилась список обычных задач");
        assertNotNull(taskManager.getEpicTasks().get(epicTaskId), "Эпик задача не добавилась список эпик задач");
        assertNotNull(taskManager.getSubTasks().get(subTaskId), "Подзадача не добавилась список подзадач");
    }
    @Test
    public void shouldChanceEpicTaskStatus() {
        taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        SubTask subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);
        taskManager.getEpicTasks().put(epicTask.getTaskId(), epicTask);
        taskManager.getSubTasks().put(subTask.getTaskId(), subTask);
        taskManager.getSubTasks().put(subTask2.getTaskId(), subTask2);

        assertEquals(epicTask.getTaskStatus(), TaskStatus.NEW, "Ожидался статус NEW");

        subTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        subTask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.setEpicTaskStatus(epicTask);
        assertEquals(epicTask.getTaskStatus(), TaskStatus.IN_PROGRESS, "Ожидался статус IN_PROGRESS");

        subTask.setTaskStatus(TaskStatus.DONE);
        subTask2.setTaskStatus(TaskStatus.DONE);
        taskManager.setEpicTaskStatus(epicTask);
        assertEquals(epicTask.getTaskStatus(), TaskStatus.DONE, "Ожидался статус DONE");

        subTask.setTaskStatus(TaskStatus.NEW);
        subTask2.setTaskStatus(TaskStatus.DONE);
        taskManager.setEpicTaskStatus(epicTask);
        assertEquals(epicTask.getTaskStatus(), TaskStatus.IN_PROGRESS, "Ожидался статус IN_PROGRESS");
    }

    @Test
    public void shouldNotIntersectTime() {
        taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        SubTask subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);
        taskManager.getEpicTasks().put(epicTask.getTaskId(), epicTask);
        taskManager.getSubTasks().put(subTask.getTaskId(), subTask);
        taskManager.getSubTasks().put(subTask2.getTaskId(), subTask2);
        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask.setDuration(Duration.ofMinutes(30));
        subTask2.setStartTime(LocalDateTime.parse("15.04.2024 12:20", DTF));
        subTask2.setDuration(Duration.ofMinutes(30));

        boolean result = (!subTask.getStartTime().isBefore(subTask2.getStartTime()) && !subTask.getStartTime().isAfter(subTask2.getEndTime()))
                || (subTask.getEndTime().isBefore(subTask2.getEndTime()) && subTask.getEndTime().isAfter(subTask2.getStartTime()));

        assertTrue(result, "Задачи не пересекаются");

        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask2.setStartTime(LocalDateTime.parse("15.04.2024 12:40", DTF));

        result = (!subTask.getStartTime().isBefore(subTask2.getStartTime()) && !subTask.getStartTime().isAfter(subTask2.getEndTime()))
                || (subTask.getEndTime().isBefore(subTask2.getEndTime()) && subTask.getEndTime().isAfter(subTask2.getStartTime()));

        assertFalse(result, "Задачи пересекаются");

        subTask.setStartTime(LocalDateTime.parse("15.04.2024 12:00", DTF));
        subTask2.setStartTime(LocalDateTime.parse("15.04.2024 12:30", DTF));
    }
}
