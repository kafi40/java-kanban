package task;

import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTaskTest {

    @Test
    public void canCreateEpicTask() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        assertNotNull(epicTask, "Ошибка создания объекта");
    }

    @Test
    public void canAddSubTaskInEpicTask() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
        epicTask.addSubTask(subTask);
        assertFalse(epicTask.getSubTasks().isEmpty(), "Список пустой");
    }

    @Test
    public void areEpicTaskNotEquals() {
        EpicTask epicTask = new EpicTask("Name", "Description");
        EpicTask epicTask2 = new EpicTask("Name", "Description");
        assertNotEquals(epicTask.equals(epicTask2), "Задачи равны");
    }
}
