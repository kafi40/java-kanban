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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static util.Parameters.DTF;
import static util.Parameters.TASK_HISTORY_BACKUP_PATH;

public class FileBackedTaskManager {
    private final HashMap<Integer, EpicTask> epicTaskHashMap = new HashMap<>();
    private static List<Task> historyList = new ArrayList<>();

    public void save(Task task, Path path) throws IOException {
        String taskType;

        if (path.equals(TASK_HISTORY_BACKUP_PATH) && historyList.contains(task)) {
            return;
        }

        if (!Files.exists(path)) {
            Files.createFile(path);
            try (FileWriter fileWriter = new FileWriter(path.toString())) {
                fileWriter.write("id,type,name,status,description,epic,startTime,duration\n");
            }
        }

        try (FileWriter fileWriter = new FileWriter(path.toString(), true)) {

            if (task.getClass() == Task.class) {
                taskType = String.valueOf(TaskType.TASK);
            } else if (task.getClass() == EpicTask.class) {
                taskType = String.valueOf(TaskType.EPICTASK);
            } else if (task.getClass() == SubTask.class) {
                taskType = String.valueOf(TaskType.SUBTASK);
            } else {
                throw new ManagerSaveException("Не удалось определить тип задачи");
            }
            fileWriter.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s%n",
                    task.getTaskId(),
                    taskType,
                    task.getTaskName(),
                    task.getTaskStatus(),
                    task.getTaskDescription(),
                    task.getClass() == SubTask.class ? ((SubTask) task).getEpicTask().getTaskId() : " ",
                    Optional.ofNullable(task.getStartTime()).map(localDateTime ->
                            localDateTime.format(DTF)).orElse(" "),
                    Optional.ofNullable(task.getDuration()).map(duration ->
                            String.valueOf(duration.toMinutes())).orElse(" ")));
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Не удалось записать задачу");
        }
    }

    public List<Task> getData(Path path) throws IOException {
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

        if (!Files.exists(path)) {
            Files.createFile(path);
            try (FileWriter fileWriter = new FileWriter(path.toString())) {
                fileWriter.write("id,type,name,status,description,epic,startTime,duration\n");
            }
        }

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
                Optional<String> optionalStartTime =
                        Optional.ofNullable(taskData.get(6));
                Optional<String> optionalDuration =
                        Optional.ofNullable(taskData.get(7));

                if (taskId < 1) {
                    throw new ManagerSaveException("ID не может быть меньше 1");
                }

                if (taskType == TaskType.TASK) {
                    task = new Task(taskName, description, taskStatus);
                    task.setTaskId(taskId);
                    if (optionalStartTime.isPresent()) {
                        if (!optionalStartTime.get().isBlank()) {
                            task.setStartTime(LocalDateTime.parse(optionalStartTime.get(), DTF));
                        }
                    }
                    if (optionalDuration.isPresent()) {
                        if (!optionalDuration.get().isBlank()) {
                            task.setDuration(Duration.ofMinutes(Long.parseLong(optionalDuration.get())));
                        }
                    }
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
                    if (optionalStartTime.isPresent()) {
                        if (!optionalStartTime.get().isBlank()) {
                            subTask.setStartTime(LocalDateTime.parse(optionalStartTime.get(), DTF));
                        }
                    }
                    if (optionalDuration.isPresent()) {
                        if (!optionalDuration.get().isBlank()) {
                            subTask.setDuration(Duration.ofMinutes(Long.parseLong(optionalDuration.get())));
                        }
                    }
                    mainEpicTask.addSubTask(subTask);
                    taskList.add(subTask);
                } else {
                    throw new ManagerSaveException("Не удалось восстановить задачу из файла");
                }

                if (InMemoryTaskManager.taskIdCounter < taskId) {
                    InMemoryTaskManager.taskIdCounter = taskId;
                }
            }
        } catch (Exception e) {
            System.out.println("Файл " + path.getFileName() + " не удалось прочесть");
        }
        if (path.equals(TASK_HISTORY_BACKUP_PATH)) {
            historyList = taskList;
        }
        return taskList;
    }
}
