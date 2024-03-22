package io;

import enums.TaskStatus;
import enums.TaskType;
import exceptions.ManagerSaveException;
import memory.InMemoryTaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager {
    private Path path;
    // Список вынес в поле, если в истории нет просмотра эпика, но есть его подзадача.
    // Подзадача подтянет эпик из списка
    private final HashMap<Integer, EpicTask> epicTaskHashMap = new HashMap<>();
    private static List<Task> historyList = new ArrayList<>();

    public void save(Task task, boolean isHistory) throws IOException {
        String taskType;
        // Проверяем есть ли такая задача в истории
        if (isHistory && historyList.contains(task)) {
            return;
        }
        // Проверяем куда сохранять задачу
        if (isHistory) {
            path = Paths.get("src/resource", "history.txt");
            historyList.add(task);
        } else {
            path = Paths.get("src/resource", "backup.txt");
        }
        // Если файл не существует, создаем его
        if (!Files.exists(path)) {
            Files.createFile(path);
            try (FileWriter fileWriter = new FileWriter(path.toString())) {
                fileWriter.write("id,type,name,status,description,epic");
            }
        }
        // Создаем объект для записи задачи в файл
        try (FileWriter fileWriter = new FileWriter(path.toString(), true)) {

            // Определяем тип задачи
            if (task.getClass() == Task.class) {
                taskType = String.valueOf(TaskType.TASK);
            } else if (task.getClass() == EpicTask.class) {
                taskType = String.valueOf(TaskType.EPICTASK);
            } else if (task.getClass() == SubTask.class) {
                taskType = String.valueOf(TaskType.SUBTASK);
            } else {
                throw new ManagerSaveException("Не удалось определить тип задачи");
            }
            fileWriter.write(String.format("%d,%s,%s,%s,%s,%s%n",
                    task.getTaskId(),
                    taskType,
                    task.getTaskName(),
                    task.getTaskStatus(),
                    task.getTaskDescription(),
                    task.getClass() == SubTask.class ? ((SubTask) task).getEpicTask().getTaskId() : ""));
        } catch (Exception e) {
            System.out.println("Не удалось записать задачу");
        }
    }

    //Получаем данные из файла
    public List<Task> getData(boolean isHistory) throws IOException {
        List<Task> taskList = new ArrayList<>();
        int taskId;
        int epicTaskId = 0;
        TaskType taskType;
        String taskName;
        String description;
        TaskStatus taskStatus;
        Task task;
        EpicTask epicTask;
        SubTask subTask;

        // Проверяем откуда читать задачу
        if (isHistory) {
            path = Paths.get("src/resource", "history.txt");
        } else {
            path = Paths.get("src/resource", "backup.txt");
        }
        // Если файл не существует, создаем его
        if (!Files.exists(path)) {
            Files.createFile(path);
            try (FileWriter fileWriter = new FileWriter(path.toString())) {
                fileWriter.write("id,type,name,status,description,epic\n");
            }
        }
        // Читаем данные из файла
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String data = bufferedReader.readLine();
                List<String> taskData = List.of(data.split(","));
                taskId = Integer.parseInt(taskData.getFirst());
                taskType = TaskType.valueOf(taskData.get(1));
                taskName = taskData.get(2);
                taskStatus = TaskStatus.valueOf(taskData.get(3));
                description = taskData.get(4);
                if (taskType == TaskType.SUBTASK) {
                    epicTaskId = Integer.parseInt(taskData.get(5));
                }
                if (taskId < 1) {
                    throw new ManagerSaveException("ID не может быть меньше 1");
                }
                // Создаем задачи и помещаем их в список
                if (taskType == TaskType.TASK) {
                    task = new Task(taskName, description, taskStatus);
                    task.setTaskId(taskId);
                    taskList.add(task);
                } else if (taskType == TaskType.EPICTASK) {
                    epicTask = new EpicTask(taskName, description);
                    epicTask.setTaskId(taskId);
                    epicTaskHashMap.put(epicTask.getTaskId(), epicTask);
                    taskList.add(epicTask);
                } else if (taskType == TaskType.SUBTASK) {
                    EpicTask mainEpicTask = epicTaskHashMap.get(epicTaskId);
                    subTask = new SubTask(taskName, description, taskStatus, mainEpicTask);
                    subTask.setTaskId(taskId);
                    mainEpicTask.addSubTask(subTask);
                    taskList.add(subTask);
                } else {
                    throw new ManagerSaveException("Не удалось восстановить задачу из файла");
                }
                // Регулируем генератор
                if (InMemoryTaskManager.taskIdCounter < taskId) {
                    InMemoryTaskManager.taskIdCounter = taskId;
                }
            }
        } catch (Exception e) {
            System.out.println("Файл " + path.getFileName() + " не удалось прочесть");
        }
        if (isHistory) {
            historyList = taskList;
        }
        return taskList;
    }


}
