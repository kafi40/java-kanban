//import interfaces.TaskHistory;
//import memory.InMemoryTaskHistory;
//import org.junit.Test;
//import task.EpicTask;
//import task.SubTask;
//import task.Task;
//import memory.InMemoryTaskManager;
//import util.Managers;
//import util.TaskStatus;
//import java.util.ArrayList;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TaskTest {
//
//    private InMemoryTaskManager inMemoryTaskManager;
//    private static final TaskHistory taskHistory = new InMemoryTaskHistory();
//    private static final Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
//    private static final EpicTask epicTask = new EpicTask("Test addNewTask", "Test addNewTask description");
//    private static final SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, epicTask);
//
//    @Test
//    public void isManagersReturnExemplar() {
//        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
//        assertNotNull(inMemoryTaskManager, "Экземпляр не создан.");
//    }
//
//    @Test
//    public void addNewTask() {
//        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
//        final int taskId = task.getTaskId();
//
//        inMemoryTaskManager.getAllTypeTasks().put(taskId, task);
//        inMemoryTaskManager.getTasks().put(taskId, task);
//
//        final Task savedTask = inMemoryTaskManager.getAllTypeTasks().get(taskId);
//
//        assertNotNull(savedTask, "Задача не найдена.");
//        assertEquals(task, savedTask, "Задачи не совпадают.");
//
//        final ArrayList<Task> allTasks = new ArrayList<>(inMemoryTaskManager.getAllTypeTasks().values());
//        final ArrayList<Task> tasks = new ArrayList<>(inMemoryTaskManager.getTasks().values());
//
//        assertNotNull(allTasks, "Задачи не возвращаются.");
//        assertEquals(1, allTasks.size(), "Неверное количество задач.");
//        assertEquals(task, allTasks.get(0), "Задачи не совпадают.");
//
//        assertNotNull(tasks, "Задачи не возвращаются.");
//        assertEquals(1, tasks.size(), "Неверное количество задач.");
//        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
//
//        assertEquals(task.getTaskId(), savedTask.getTaskId(), "Изменение ID");
//        assertEquals(task.getTaskName(), savedTask.getTaskName(), "Изменение имени");
//        assertEquals(task.getTaskDescription(), savedTask.getTaskDescription(), "Изменение описания");
//        assertEquals(task.getTaskStatus(), savedTask.getTaskStatus(), "Изменение статуса");
//    }
//
//    @Test
//    public void addNewEpicTask() {
//        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
//        final int taskId = epicTask.getTaskId();
//
//        inMemoryTaskManager.getAllTypeTasks().put(taskId, epicTask);
//        inMemoryTaskManager.getEpicTasks().put(taskId, epicTask);
//
//        final Task savedTask = inMemoryTaskManager.getAllTypeTasks().get(taskId);
//        final EpicTask savedEpicTask = inMemoryTaskManager.getEpicTasks().get(taskId);
//
//        assertNotNull(savedTask, "Задача не найдена.");
//        assertEquals(epicTask, savedTask, "Задачи не совпадают.");
//
//        final ArrayList<Task> allTasks = new ArrayList<>(inMemoryTaskManager.getAllTypeTasks().values());
//        final ArrayList<EpicTask> epicTasks = new ArrayList<>(inMemoryTaskManager.getEpicTasks().values());
//
//        assertNotNull(allTasks, "Задачи не возвращаются.");
//        assertEquals(1, allTasks.size(), "Неверное количество задач.");
//        assertEquals(epicTask, allTasks.get(0), "Задачи не совпадают.");
//
//        assertNotNull(epicTasks, "Задачи не возвращаются.");
//        assertEquals(1, epicTasks.size(), "Неверное количество задач.");
//        assertEquals(epicTask, epicTasks.get(0), "Задачи не совпадают.");
//
//        assertEquals(epicTask.getTaskId(), savedTask.getTaskId(), "Изменение ID");
//        assertEquals(epicTask.getTaskName(), savedTask.getTaskName(), "Изменение имени");
//        assertEquals(epicTask.getTaskDescription(), savedTask.getTaskDescription(), "Изменение описания");
//        assertEquals(epicTask.getTaskStatus(), savedTask.getTaskStatus(), "Изменение статуса");
//        assertEquals(epicTask.getSubTasks(), savedEpicTask.getSubTasks(), "Изменение подзадач");
//
//    }
//
//    @Test
//    public void addNewSubTask() {
//        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
//        final int taskId = subTask.getTaskId();
//
//        inMemoryTaskManager.getAllTypeTasks().put(taskId, subTask);
//        inMemoryTaskManager.getSubTasks().put(taskId, subTask);
//
//        final Task savedTask = inMemoryTaskManager.getAllTypeTasks().get(taskId);
//        final SubTask savedSubTask = inMemoryTaskManager.getSubTasks().get(taskId);
//
//        assertNotNull(savedTask, "Задача не найдена.");
//        assertEquals(subTask, savedTask, "Задачи не совпадают.");
//
//        final ArrayList<Task> allTasks = new ArrayList<>(inMemoryTaskManager.getAllTypeTasks().values());
//        final ArrayList<SubTask> supTasks = new ArrayList<>(inMemoryTaskManager.getSubTasks().values());
//
//        assertNotNull(allTasks, "Задачи не возвращаются.");
//        assertEquals(1, allTasks.size(), "Неверное количество задач.");
//        assertEquals(subTask, allTasks.get(0), "Задачи не совпадают.");
//
//        assertNotNull(supTasks, "Задачи не возвращаются.");
//        assertEquals(1, supTasks.size(), "Неверное количество задач.");
//        assertEquals(subTask, supTasks.get(0), "Задачи не совпадают.");
//
//        assertEquals(subTask.getTaskId(), savedTask.getTaskId(), "Изменение ID");
//        assertEquals(subTask.getTaskName(), savedTask.getTaskName(), "Изменение имени");
//        assertEquals(subTask.getTaskDescription(), savedTask.getTaskDescription(), "Изменение описания");
//        assertEquals(subTask.getTaskStatus(), savedTask.getTaskStatus(), "Изменение статуса");
//        assertEquals(subTask.getEpicTask(), savedSubTask.getEpicTask(), "Изменение подзадач");
//
//    }
//
//    @Test
//    public void addHistory() {
//        final ArrayList<Task> history = taskHistory.getHistory();
//
//        taskHistory.addTaskInHistory(task);
//        assertNotNull(history, "История не пустая.");
//        assertEquals(1, history.size(), "История не пустая.");
//
//        taskHistory.addTaskInHistory(epicTask);
//        assertNotNull(history, "История не пустая.");
//        assertEquals(2, history.size(), "История не пустая.");
//
//        taskHistory.addTaskInHistory(subTask);
//        assertNotNull(history, "История не пустая.");
//        assertEquals(3, history.size(), "История не пустая.");
//    }
//}