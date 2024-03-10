package task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import util.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTaskTest {
    public static EpicTask epicTask;
    public static EpicTask epicTask2;
    public static SubTask subTask;

    @BeforeAll
    public static void beforeAll() {
        epicTask = new EpicTask("Name", "Description");
        epicTask2 = new EpicTask("Name", "Description");
    }
    @Test
    public void canCreateEpicTask() {
        epicTask = new EpicTask("Name", "Description");
        epicTask2 = new EpicTask("Name", "Description");
        assertNotNull(epicTask, "Ошибка создания объекта");
    }

    @Test
    public void canAddSubTaskInEpicTask() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
        epicTask.addSubTask(subTask);
        assertFalse(epicTask.getSubTasks().isEmpty(), "Список пустой");
    }
    @Test
    public void AreEpicTaskNotEquals() {
        assertNotEquals(epicTask.equals(epicTask2), "Задачи равны");
    }

}
