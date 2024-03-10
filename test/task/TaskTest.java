package task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    public static Task task;
    public static Task task2;

    @BeforeAll
    public static void beforeAll() {
        task = new Task("Name", "Description", TaskStatus.NEW);
        task2 = new Task("Name", "Description", TaskStatus.NEW);
    }
    @Test
    public void canCreateTask() {
        assertNotNull(task, "Ошибка создания объекта");
    }
    @Test
    public void AreTaskNotEquals() {
        assertNotEquals(task.equals(task2), "Задачи равны");
    }
}