package task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    public static EpicTask epicTask;
    public static SubTask subTask;
    public static SubTask subTask2;

    @BeforeAll
    public static void beforeAll() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
        subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
    }

    @Test
    public void canCreateSubTask() {
        assertNotNull(subTask, "Ошибка создания объекта");
    }

    @Test
    public void AreSubTaskNotEquals() {
        assertNotEquals(subTask.equals(subTask2), "Задачи равны");
    }
}
