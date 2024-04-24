package task;

import memory.InMemoryTaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {

    @Test
    public void canCreateSubTask() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertNotNull(subTask, "Ошибка создания объекта");
    }

    @Test
    public void areSubTaskNotEquals() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        SubTask subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertNotEquals(subTask.equals(subTask2), "Задачи равны");
    }
    @Test
    public void shouldHasEpicTask() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        assertEquals(subTask.getEpicTaskId(), epicTask.getTaskId(), "Не является эпиком для подзадачи");
    }
}
