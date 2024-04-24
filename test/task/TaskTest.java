package task;

import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void canCreateTask() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        assertNotNull(task, "Ошибка создания объекта");
    }

    @Test
    public void areTaskNotEquals() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        assertNotEquals(task.equals(task2), "Задачи равны");
    }
}