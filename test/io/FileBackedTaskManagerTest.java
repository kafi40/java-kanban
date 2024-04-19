//package io;
//
//import enums.TaskStatus;
//import memory.InMemoryTaskHistory;
//import memory.InMemoryTaskManager;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import task.EpicTask;
//import task.SubTask;
//import task.Task;
//import util.Managers;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FileBackedTaskManagerTest {
//    public static InMemoryTaskManager taskManager;
//    public static InMemoryTaskHistory taskHistory;
//    static public Task task;
//    static public EpicTask epicTask;
//    static public SubTask subTask;
//    public List tasksList;
//
//    @BeforeAll
//    public static void beforeAll() {
//        taskManager = (InMemoryTaskManager) Managers.getDefault();
//        taskHistory = new InMemoryTaskHistory();
//        task = new Task("Name", "Description", TaskStatus.NEW);
//        epicTask = new EpicTask("Name", "Description");
//        subTask = new SubTask("Name", "Description", TaskStatus.NEW, epicTask);
//    }
//
//    @Test
//    public void canSaveAndLoadTaskHistoryInFile() {
//        taskHistory.addTaskInHistory(task);
//        taskHistory.addTaskInHistory(epicTask);
//        taskHistory.addTaskInHistory(subTask);
//        tasksList = taskHistory.getHistory();
//        try {
//            File tempFile = File.createTempFile("history", ".txt", new File("src/resource"));
//            try (FileWriter fileWriter = new FileWriter(tempFile)) {
//                fileWriter.write("id,type,name,status,description,epic\n");
//            }
//
//            new FileBackedTaskManager().save(task, tempFile.toPath());
//            new FileBackedTaskManager().save(epicTask, tempFile.toPath());
//            new FileBackedTaskManager().save(subTask, tempFile.toPath());
//            List<Task> tasksListBackup = new FileBackedTaskManager().getData(tempFile.toPath());
//
//            assertArrayEquals(tasksListBackup.toArray(), tasksList.toArray());
//            tempFile.deleteOnExit();
//        } catch (Exception e) {
//            System.out.println("Ошибка: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void canSaveAndLoadTaskInFile() {
//        List<Task> taskList = new ArrayList<>();
//        taskList.add(task);
//        taskList.add(epicTask);
//        taskList.add(subTask);
//        try {
//            File tempFile = File.createTempFile("backup", ".txt", new File("src/resource"));
//            try (FileWriter fileWriter = new FileWriter(tempFile)) {
//                fileWriter.write("id,type,name,status,description,epic\n");
//            }
//
//            new FileBackedTaskManager().save(task, tempFile.toPath());
//            new FileBackedTaskManager().save(epicTask, tempFile.toPath());
//            new FileBackedTaskManager().save(subTask, tempFile.toPath());
//            List<Task> tasksListBackup = new FileBackedTaskManager().getData(tempFile.toPath());
//
//            assertArrayEquals(tasksListBackup.toArray(), taskList.toArray());
//            tempFile.deleteOnExit();
//        } catch (Exception e) {
//            System.out.println("Ошибка: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateFileException() {
//        assertThrows(IOException.class, () -> {
//            File tempFile = File.createTempFile("backup", "", new File("sr/resource"));
//            tempFile.deleteOnExit();
//        }, "Не корректное создание файла должно привести к исключению");
//    }
//
//    @Test
//    public void testCreateFileWriter() {
//        assertThrows(IOException.class, () -> {
//            FileWriter fileWriter = new FileWriter("");
//            fileWriter.write("Какой-то текст");
//        }, "Не корректно созданный экземпляр FileWriter должен привести к исключению");
//    }
//
//    @Test
//    public void testCreateBufferReader() {
//        assertThrows(IOException.class, () -> {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(""));
//            bufferedReader.readLine();
//        }, "Не корректно созданный экземпляр BufferedReader должен привести к исключению");
//    }
//}
