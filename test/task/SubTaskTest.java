package task;

import org.junit.jupiter.api.Test;
import util.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    public static EpicTask epicTask;
    public static SubTask subTask;
    public static SubTask subTask2;

    @Test
    public void canCreateSubTask() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
        assertNotNull(subTask, "Ошибка создания объекта");
    }

    @Test
    public void areSubTaskNotEquals() {
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
        subTask2 = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
        assertNotEquals(subTask.equals(subTask2), "Задачи равны");
    }
}
