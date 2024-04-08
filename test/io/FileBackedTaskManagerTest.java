package io;

import enums.TaskStatus;
import memory.InMemoryTaskHistory;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class FileBackedTaskManagerTest {
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

    @Test
    public void canSaveAndLoadTaskHistoryInFile() throws IOException {
        taskHistory.addTaskInHistory(task);
        taskHistory.addTaskInHistory(epicTask);
        taskHistory.addTaskInHistory(subTask);
        tasksList = taskHistory.getHistory();

        try {
            Files.delete(Path.of("src/resource", "history.txt"));
        } catch (IOException e) {

        }
        new FileBackedTaskManager().save(task, true);
        new FileBackedTaskManager().save(epicTask, true);
        new FileBackedTaskManager().save(subTask, true);
        List<Task>tasksListBackup = new FileBackedTaskManager().getData(true);

        assertArrayEquals(tasksListBackup.toArray(), tasksList.toArray());
        Files.delete(Path.of("src/resource", "history.txt"));
    }

    @Test
    public void canSaveAndLoadTaskInFile() throws IOException {
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        taskList.add(epicTask);
        taskList.add(subTask);

        try {
            Files.delete(Path.of("src/resource", "backup.txt"));
        } catch (Exception e) {

        }
        new FileBackedTaskManager().save(task, false);
        new FileBackedTaskManager().save(epicTask, false);
        new FileBackedTaskManager().save(subTask, false);
        List<Task>tasksListBackup = new FileBackedTaskManager().getData(false);

        assertArrayEquals(tasksListBackup.toArray(), taskList.toArray());
        Files.delete(Path.of("src/resource", "backup.txt"));
    }
}
