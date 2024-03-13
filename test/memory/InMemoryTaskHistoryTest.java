package memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import util.TaskStatus;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskHistoryTest {

    public static InMemoryTaskManager taskManager;
    public static InMemoryTaskHistory taskHistory;
    static public Task task;
    static public EpicTask epicTask;
    static public SubTask subTask;
    public List tasksList;

    @BeforeAll
    public static void beforeAll() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        taskHistory = new InMemoryTaskHistory();
        task = new Task("Name", "Description", TaskStatus.NEW);
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
    }

    @BeforeEach
    public void shouldAddTasksInHistory() {
        taskHistory.addTaskInHistory(task);
        tasksList = taskHistory.getHistory();
        assertEquals(1, tasksList.size(), "Ожидается 1 элемент");
        taskHistory.addTaskInHistory(epicTask);
        tasksList = taskHistory.getHistory();
        assertEquals(2, tasksList.size(), "Ожидается 2 элемента");
        taskHistory.addTaskInHistory(subTask);
        tasksList = taskHistory.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
    }

    @Test
    public void shouldNotRepeatTaskInTaskHistory() {
        Task task1 = task;
        EpicTask epicTask1 = epicTask;
        SubTask subTask1 = subTask;

        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
        taskHistory.addTaskInHistory(task1);
        taskHistory.addTaskInHistory(epicTask1);
        taskHistory.addTaskInHistory(subTask1);
        tasksList = taskHistory.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");

    }
}