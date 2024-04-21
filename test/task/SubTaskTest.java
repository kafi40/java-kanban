package task;

import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    public static EpicTask epicTask;
    public static SubTask subTask;
    public static SubTask subTask2;

    @Test
    public void canCreateSubTask() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertNotNull(subTask, "Ошибка создания объекта");
    }

    @Test
    public void areSubTaskNotEquals() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertNotEquals(subTask.equals(subTask2), "Задачи равны");
    }
    @Test
    public void shouldHasEpicTask() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertEquals(subTask.getEpicTaskId(), epicTask.getTaskId(), "Не является эпиком для подзадачи");
    }
}
