package io;

import enums.TaskStatus;
import enums.TaskType;
import interfaces.TaskManager;
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
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static Path path;
    private Task task;

    public void save(Task task) throws IOException {
        String taskType = "";
        // Сохраняем в переменную путь к файлу
        path = Paths.get("src/resource", "backup.txt");
        // Если файл не существует, создаем его
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        // Создаем объект для записи задачи в файл
        try (FileWriter fileWriter = new FileWriter(path.toString(), true)) {
            // Определяем тип задачи
            if (task.getClass() == Task.class) {
                taskType = String.valueOf(TaskType.TASK);
            } else if (task.getClass() == EpicTask.class){
                taskType = String.valueOf(TaskType.EPICTASK);
            } else if (task.getClass() == SubTask.class) {
                taskType = String.valueOf(TaskType.SUBTASK);
            }

            fileWriter.write(String.format("%d,%s,%s,%s,%s,%s%n",
                    task.getTaskId(),
                    taskType,
                    task.getTaskName(),
                    task.getTaskStatus(),
                    task.getTaskDescription(),
                    task.getClass() == SubTask.class ? ((SubTask) task).getEpicTask().getTaskId() : ""));
        }
    }

    @Override
    public void addTask(int command) throws IOException {
        super.addTask(command);
    }


    public List<Task> getData() throws IOException {
        List<Task> taskList = new ArrayList<>();
        int id;
        int epicTaskId;
        TaskType taskType;
        String name;
        String description;
        TaskStatus taskStatus;
        Task task;
        EpicTask epicTask = null;
        SubTask subTask;

        path = Paths.get("src/resource", "backup.txt");
        // Если файл не существует, создаем его
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        // Читаем данные из файла
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()));) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                List<String> taskData = List.of(s.split(","));
                id = Integer.parseInt(taskData.getFirst());
                taskType = TaskType.valueOf(taskData.get(1));
                name = taskData.get(2);
                taskStatus = TaskStatus.valueOf(taskData.get(3));
                description = taskData.get(4);
                if (taskType == TaskType.SUBTASK) {
                    epicTaskId = Integer.parseInt(taskData.get(5));
                }
                // Создаем задачи и помещаем их в список
                if (taskType == TaskType.TASK) {
                    task = new Task(name, description, taskStatus);
                    task.setTaskId(id);
                    taskList.add(task);
                } else if (taskType == TaskType.EPICTASK) {
                    epicTask = new EpicTask(name, description);
                    epicTask.setTaskId(id);
                    taskList.add(epicTask);
                } else if (taskType == TaskType.SUBTASK) {
                    subTask = new SubTask(name, description, taskStatus, epicTask);
                    subTask.setTaskId(id);
                    taskList.add(subTask);
                }
                // Регулируем генератор
                InMemoryTaskManager.taskIdCounter = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }
    @Override
    public String toString() {
        return "FileBackedTaskManager{}";
    }
}
