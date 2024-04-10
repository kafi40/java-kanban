package task;

import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTaskTest {
    public EpicTask epicTask;
    public EpicTask epicTask2;
    public SubTask subTask;

    @Test
    public void canCreateEpicTask() {
        epicTask = new EpicTask("Name", "Description");
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
    public void areEpicTaskNotEquals() {
        epicTask = new EpicTask("Name", "Description");
        epicTask2 = new EpicTask("Name", "Description");
        assertNotEquals(epicTask.equals(epicTask2), "Задачи равны");
    }
}
