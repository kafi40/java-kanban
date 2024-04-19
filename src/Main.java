import enums.TaskStatus;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import io.FileBackedTaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.Parameters.DTF;
import static util.Parameters.FILE;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager backedManager = FileBackedTaskManager.loadFromFile(FILE);

        Task task1 = new Task("Задача 1", "Задача на 10:00 с длительностью час", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.parse("05.06.2024 10:00", DTF));
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("Задача 2", "Задача на 17:30 с длительностью час", TaskStatus.IN_PROGRESS);
        task2.setStartTime(LocalDateTime.parse("05.06.2024 17:30", DTF));
        task2.setDuration(Duration.ofMinutes(60));

        Task task3 = new Task("Задача 3", "Задача на 19:30 с длительностью час", TaskStatus.IN_PROGRESS);
        task3.setStartTime(LocalDateTime.parse("05.06.2024 19:30", DTF));
        task3.setDuration(Duration.ofMinutes(60));

        EpicTask epicTask1 = new EpicTask("Сложная задача 1", "Сложная задача с двумя подзадачами");

        SubTask subTask1 = new SubTask("Подзадача 1", "Подзадача сложной задачи 1 на 12:00", TaskStatus.NEW, epicTask1);
        subTask1.setStartTime(LocalDateTime.parse("05.06.2024 12:00", DTF));
        subTask1.setDuration(Duration.ofMinutes(30));

        SubTask subTask2 = new SubTask("Подзадача 2", "Подзадача сложной задачи 1 на 14:00", TaskStatus.NEW, epicTask1);
        subTask2.setStartTime(LocalDateTime.parse("05.06.2024 14:00", DTF));
        subTask2.setDuration(Duration.ofMinutes(60));

        EpicTask epicTask2 = new EpicTask("Сложная задача 2", "Сложная задача с одной подзадачей");

        SubTask subTask3 = new SubTask("Подзадача 3", "Подзадача сложной задачи 2 на 13:01", TaskStatus.IN_PROGRESS, epicTask2);
        subTask3.setStartTime(LocalDateTime.parse("05.06.2024 13:01", DTF));
        subTask3.setDuration(Duration.ofMinutes(30));

        backedManager.addTask(task1);
        backedManager.addTask(task2);
        backedManager.addEpicTask(epicTask1);
        backedManager.addSubTask(subTask1);
        backedManager.addSubTask(subTask2);
        backedManager.addEpicTask(epicTask2);
        backedManager.addSubTask(subTask3);

        backedManager.deleteSubTask(5);


//        printAllTasks(backedManager);
        System.out.println(backedManager.getPrioritizedTasks());


    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        taskManager.getTasks().values().forEach(System.out::println);
        System.out.println("Эпики:");
        taskManager.getEpicTasks().values().forEach(
                epicTask -> {
                    System.out.println(epicTask);
                    epicTask.getSubTasks().forEach(System.out::println);
                }
        );
        System.out.println("Подзадачи:");
        taskManager.getSubTasks().values().forEach(System.out::println);
    }
}