package io;

import enums.TaskStatus;
import memory.InMemoryTaskHistory;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static util.Parameters.*;

public class FileBackedTaskManagerTest {
    public static InMemoryTaskManager taskManager;
    public static InMemoryTaskHistory taskHistory;
    static public Task task;
    static public EpicTask epicTask;
    static public SubTask subTask;
    public List tasksList;
    public static final Path TASK_BACKUP_PATH_TEST = Path.of("src/resource", "backupTest.txt");
    public static final Path TASK_HISTORY_BACKUP_PATH_TEST = Path.of("src/resource", "historyTest.txt");

    @BeforeAll
    public static void beforeAll() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        taskHistory = new InMemoryTaskHistory();
        task = new Task("Name", "Description", TaskStatus.NEW);
        epicTask = new EpicTask("Name", "Description");
        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
    }

    @Test
    public void canSaveAndLoadTaskHistoryInFile() throws IOException {
        taskHistory.addTaskInHistory(task);
        taskHistory.addTaskInHistory(epicTask);
        taskHistory.addTaskInHistory(subTask);
        tasksList = taskHistory.getHistory();

        new FileBackedTaskManager().save(task, TASK_HISTORY_BACKUP_PATH_TEST);
        new FileBackedTaskManager().save(epicTask, TASK_HISTORY_BACKUP_PATH_TEST);
        new FileBackedTaskManager().save(subTask, TASK_HISTORY_BACKUP_PATH_TEST);
        List<Task>tasksListBackup = new FileBackedTaskManager().getData(TASK_HISTORY_BACKUP_PATH_TEST);

        assertArrayEquals(tasksListBackup.toArray(), tasksList.toArray());
        Files.delete(TASK_HISTORY_BACKUP_PATH_TEST);
    }

    @Test
    public void canSaveAndLoadTaskInFile() throws IOException {
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        taskList.add(epicTask);
        taskList.add(subTask);

        new FileBackedTaskManager().save(task, TASK_BACKUP_PATH_TEST);
        new FileBackedTaskManager().save(epicTask, TASK_BACKUP_PATH_TEST);
        new FileBackedTaskManager().save(subTask, TASK_BACKUP_PATH_TEST);
        List<Task>tasksListBackup = new FileBackedTaskManager().getData(TASK_BACKUP_PATH_TEST);

        assertArrayEquals(tasksListBackup.toArray(), taskList.toArray());
        Files.delete(TASK_BACKUP_PATH_TEST);
    }
}
