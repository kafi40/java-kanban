package memory;

import enums.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskHistoryTest {

    public static InMemoryTaskManager taskManager;
    public static InMemoryHistoryManager historyManager;
    static public Task task;
    static public EpicTask epicTask;
    static public SubTask subTask;
    public List tasksList;

    @BeforeAll
    public static void beforeAll() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = new InMemoryHistoryManager();
        task = new Task("Name", "Description", TaskStatus.NEW);
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
    }

    @BeforeEach
    public void shouldAddTasksInHistory() {
        historyManager.add(task);
        tasksList = historyManager.getHistory();
        assertEquals(1, tasksList.size(), "Ожидается 1 элемент");
        historyManager.add(epicTask);
        tasksList = historyManager.getHistory();
        assertEquals(2, tasksList.size(), "Ожидается 2 элемента");
        historyManager.add(subTask);
        tasksList = historyManager.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
    }

    @Test
    public void shouldNotRepeatTaskInTaskHistory() {
        Task task1 = task;
        EpicTask epicTask1 = epicTask;
        SubTask subTask1 = subTask;

        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
        historyManager.add(task1);
        historyManager.add(epicTask1);
        historyManager.add(subTask1);
        tasksList = historyManager.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
    }
    @AfterAll
    public static void afterAll() {
        InMemoryTaskManager.taskIdCounter = 0;
    }
}