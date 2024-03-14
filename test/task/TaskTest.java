package task;

import org.junit.jupiter.api.Test;
import enums.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    public static Task task;
    public static Task task2;

    @Test
    public void canCreateTask() {
        task = new Task("Name", "Description", TaskStatus.NEW);
        assertNotNull(task, "Ошибка создания объекта");
    }

    @Test
    public void areTaskNotEquals() {
        task = new Task("Name", "Description", TaskStatus.NEW);
        task2 = new Task("Name", "Description", TaskStatus.NEW);
        assertNotEquals(task.equals(task2), "Задачи равны");
    }
}