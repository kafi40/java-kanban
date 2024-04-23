package memory;

import enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskHistoryTest {

    public static InMemoryTaskManager taskManager;
    public static InMemoryHistoryManager historyManager;
    public List tasksList;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksInHistory() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());
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
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        EpicTask epicTask = new EpicTask("Name", "Description");
        SubTask subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask.getTaskId());

        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);
        tasksList = historyManager.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);
        tasksList = historyManager.getHistory();
        assertEquals(3, tasksList.size(), "Ожидается 3 элемента");
    }
}