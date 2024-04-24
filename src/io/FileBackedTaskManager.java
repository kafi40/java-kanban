package io;

import enums.TaskStatus;
import enums.TaskType;
import exceptions.ManagerSaveException;
import memory.InMemoryTaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static util.Parameters.DTF;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toString()))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String data = bufferedReader.readLine();
                Task task = fileBackedTaskManager.fromString(data);
                if (task.getClass() == Task.class) {
                    fileBackedTaskManager.tasks.put(task.getTaskId(), task);
                }
                if (task.getClass() == EpicTask.class) {
                    fileBackedTaskManager.epicTasks.put(task.getTaskId(), (EpicTask) task);
                }
                if (task.getClass() == SubTask.class) {
                    fileBackedTaskManager.subTasks.put(task.getTaskId(), (SubTask) task);
                    EpicTask mainEpicTask = fileBackedTaskManager.epicTasks.get(((SubTask) task).getEpicTaskId());
                    mainEpicTask.addSubTask((SubTask) task);
                    fileBackedTaskManager.setEpicTaskDateTime(mainEpicTask);
                }
            }
            return fileBackedTaskManager;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не удалось найти файл!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpicTask(int id) {
        super.deleteEpicTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    private void save() {
        if (!Files.exists(file.toPath())) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,description,status,epic,startTime,duration\n");
            for (Task task : tasks.values()) {
                fileWriter.write(toString(task));
            }
            for (EpicTask epicTask : epicTasks.values()) {
                fileWriter.write(toString(epicTask));
                for (SubTask subTask : epicTask.getSubTasks()) {
                    fileWriter.write(toString(subTask));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toString(Task task) throws ManagerSaveException {
        String taskType;

        if (task.getClass() == Task.class) {
            taskType = String.valueOf(TaskType.TASK);
        } else if (task.getClass() == EpicTask.class) {
            taskType = String.valueOf(TaskType.EPICTASK);
        } else if (task.getClass() == SubTask.class) {
            taskType = String.valueOf(TaskType.SUBTASK);
        } else {
            throw new ManagerSaveException("Не удалось определить тип задачи");
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s%n",
                task.getTaskId(),
                taskType,
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStatus(),
                task.getClass() == SubTask.class ? ((SubTask) task).getEpicTaskId() : " ",
                Optional.ofNullable(task.getStartTime()).map(localDateTime ->
                        localDateTime.format(DTF)).orElse(" "),
                Optional.ofNullable(task.getDuration()).map(duration ->
                        String.valueOf(duration.toMinutes())).orElse(" "));
    }

    public Task fromString(String data) throws ManagerSaveException {
        int taskId;
        int epicTaskId = 0;
        TaskType taskType;
        String taskName;
        String description;
        TaskStatus taskStatus;
        Task task;
        EpicTask epicTask;
        SubTask subTask;

        List<String> taskData = List.of(data.split(","));
        taskId = Integer.parseInt(taskData.getFirst());
        if (taskId < 1) {
            throw new ManagerSaveException("ID не может быть меньше 1");
        }
        taskType = TaskType.valueOf(taskData.get(1));
        taskName = taskData.get(2);
        description = taskData.get(3);
        taskStatus = TaskStatus.valueOf(taskData.get(4));
        if (taskType == TaskType.SUBTASK) {
            epicTaskId = Integer.parseInt(taskData.get(5));
        }
        Optional<String> optionalStartTime =
                Optional.ofNullable(taskData.get(6));
        Optional<String> optionalDuration =
                Optional.ofNullable(taskData.get(7));

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
            return task;
        } else if (taskType == TaskType.EPICTASK) {
            epicTask = new EpicTask(taskName, description);
            epicTask.setTaskId(taskId);
            return epicTask;
        } else if (taskType == TaskType.SUBTASK) {
            subTask = new SubTask(taskName, description, taskStatus, epicTaskId);
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
            return subTask;
        } else {
            throw new ManagerSaveException("Не удалось восстановить задачу из файла");
        }
    }
}
