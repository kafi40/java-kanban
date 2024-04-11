package memory;

import enums.TaskStatus;
import interfaces.TaskHistory;
import interfaces.TaskManager;
import io.FileBackedTaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.UserInterface;
import util.Utils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static util.Parameters.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> allTypeTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final TreeSet<Task> taskTreeSet = new TreeSet<>();
    private boolean isSorted = false;
    public static int taskIdCounter = 0;
    private final TaskHistory taskHistory = new InMemoryTaskHistory();

    public HashMap<Integer, Task> getAllTypeTasks() {
        return allTypeTasks;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public TaskHistory getTaskHistory() {
        return taskHistory;
    }

    public static int taskIdGenerator() {
       taskIdCounter = taskIdCounter + 1;
       return taskIdCounter;
    }

    @Override
    public TaskStatus setTaskStatus(int command) {
        return switch (command) {
            case 2 -> TaskStatus.IN_PROGRESS;
            case 3 -> TaskStatus.DONE;
            default -> TaskStatus.NEW;
        };
    }

    @Override
    public void setEpicTaskStatus() {
        boolean isNewTaskFlag = false;
        boolean isDoneTaskFlag = false;

        for (EpicTask et : epicTasks.values()) {
            if (et.getSubTasks().isEmpty()) {
                et.setTaskStatus(TaskStatus.NEW);
                continue;
            }
            for (SubTask st : et.getSubTasks()) {
                if (st.getTaskStatus().equals(TaskStatus.NEW)) {
                    isNewTaskFlag = true;
                } else {
                    isNewTaskFlag = false;
                    break;
                }
            }
            for (SubTask st : et.getSubTasks()) {
                if (st.getTaskStatus().equals(TaskStatus.DONE)) {
                    isDoneTaskFlag = true;
                } else {
                    isDoneTaskFlag = false;
                    break;
                }
            }
            if (isNewTaskFlag) {
                et.setTaskStatus(TaskStatus.NEW);
            } else if (isDoneTaskFlag) {
                et.setTaskStatus(TaskStatus.DONE);
            } else {
                et.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void showTask(Task task) {
        String taskStatus;
        String epicTask;

        if (task.getClass() == SubTask.class) {
            epicTask = ((SubTask) task).getEpicTask().getTaskName();
        } else {
            epicTask = " ";
        }
        taskStatus = switch (task.getTaskStatus()) {
            case NEW -> "Новая";
            case IN_PROGRESS -> "В процессе";
            case DONE -> "Выполнена";
        };
        System.out.printf("%-10d %-30s %-30s %-15s %-30s %-20s %-20s\n",
                task.getTaskId(),
                task.getTaskName(),
                task.getTaskDescription(),
                taskStatus,
                epicTask,
                Optional.ofNullable(task.getStartTime()).map(localDateTime ->
                        localDateTime.format(DTF)).orElse(""),
                Optional.ofNullable(task.getEndTime()).map(localDateTime ->
                        localDateTime.format(DTF)).orElse("")
        );
    }

    @Override
    public void addTask(int command) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String taskName;
        String taskDescription;
        TaskStatus taskStatus;
        LocalDateTime startTime;
        int durationAsInt;

        System.out.print("Введите название: ");
        taskName = scanner.nextLine();
        if (taskName.equals("0")) return;
        System.out.print("Введите описание: ");
        taskDescription = scanner.nextLine();
        if (taskDescription.equals("0")) return;
        switch (command) {
            case 1:

                UserInterface.statusMenuPrint();
                System.out.print("Выберите статус задачи: ");
                command = Utils.checkCommand(TASK_STATUS_COMMAND_COUNT);
                if (command == 0) return;
                taskStatus = setTaskStatus(command);
                Task task = new Task(taskName, taskDescription, taskStatus);
                System.out.println("Установите дату и время начала задачи");
                startTime = Utils.localDateTimeFormatter();
                task.setStartTime(startTime);
                System.out.println("Enter - пропустить");
                System.out.print("Введите продолжительность задачи в минутах: ");
                Optional<Integer> optionalDuration = Utils.checkDuration();
                if (optionalDuration.isPresent()) {
                    durationAsInt = optionalDuration.get();
                    if (durationAsInt == 0) return;
                    task.setDuration(Duration.ofMinutes(durationAsInt));
                }
                tasks.put(task.getTaskId(), task);
                allTypeTasks.put(task.getTaskId(), task);
                new FileBackedTaskManager().save(task, TASK_BACKUP_PATH);
                break;

            case 2:
                EpicTask epicTask = new EpicTask(taskName, taskDescription);
                epicTasks.put(epicTask.getTaskId(), epicTask);
                allTypeTasks.put(epicTask.getTaskId(), epicTask);
                new FileBackedTaskManager().save(epicTask, TASK_BACKUP_PATH);
                break;

            case 3:
                UserInterface.statusMenuPrint();
                System.out.print("Выберите статус задачи: ");
                command = Utils.checkCommand(TASK_STATUS_COMMAND_COUNT);
                if (command == 0) return;
                taskStatus = setTaskStatus(command);
                int i = 0;
                ArrayList<Integer> epicTasksIdList = new ArrayList<>();
                for (Task t: epicTasks.values()) {
                    i++;
                    epicTasksIdList.add(t.getTaskId());
                    System.out.println(i + " - " + t.getTaskName());
                }
                System.out.print("Выберите основную задачу: ");
                command =  Utils.checkCommand(epicTasksIdList.size() + 1);
                if (command == 0) return;
                int mainEpicTaskId = epicTasksIdList.get(command - 1);
                SubTask subTask = new SubTask(taskName, taskDescription, taskStatus, epicTasks.get(mainEpicTaskId));
                System.out.println("Enter - пропустить");
                System.out.print("Введите продолжительность задачи в минутах: ");
                optionalDuration = Utils.checkDuration();
                if (optionalDuration.isPresent()) {
                    durationAsInt = optionalDuration.get();
                    if (durationAsInt == 0) return;
                    subTask.setDuration(Duration.ofMinutes(durationAsInt));
                }
                subTasks.put(subTask.getTaskId(), subTask);
                epicTasks.get(mainEpicTaskId).addSubTask(subTask);
                allTypeTasks.put(subTask.getTaskId(), subTask);
                new FileBackedTaskManager().save(subTask, TASK_BACKUP_PATH);
        }
        isSorted = false;
        System.out.println("Задача добавлена!");
    }

    @Override
    public void clearTasks(int command) {
        switch (command) {
            case 1:
                tasks.clear();
                epicTasks.clear();
                subTasks.clear();
                allTypeTasks.clear();
                break;

            case 2:
                tasks.clear();
                break;

            case 3:
                epicTasks.clear();
                subTasks.clear();
                break;

            case 4:
                subTasks.clear();
                break;

            case 0:
                break;
        }
    }

    @Override
    public void findTaskById(int taskId) throws IOException {
        Scanner scanner = new Scanner(System.in);

        if (taskId == 0) return;
        Task task;
        if (tasks.get(taskId) != null) {
            task = tasks.get(taskId);
        } else if (epicTasks.get(taskId) != null) {
            task = epicTasks.get(taskId);
        } else if (subTasks.get(taskId) != null) {
            task = subTasks.get(taskId);
        } else {
            System.out.println("Такой задачи нет!");
            return;
        }
        UserInterface.tasksHeaderPrint();
        showTask(task);
        taskHistory.addTaskInHistory(task);
        new FileBackedTaskManager().save(task, TASK_HISTORY_BACKUP_PATH);
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    @Override
    public void deleteTaskById(int taskId) {
        allTypeTasks.remove(taskId);
        if (tasks.get(taskId) != null) {
            tasks.remove(taskId);
        } else if (epicTasks.get(taskId) != null) {
            epicTasks.get(taskId).removeSubTask();
            epicTasks.remove(taskId);
        } else if (subTasks.get(taskId) != null) {
            subTasks.remove(taskId);
        } else {
            System.out.println("Такой задачи нет!");
            return;
        }
        System.out.println("Задача удалена!");
    }

    @Override
    public void showTasks(int command) {

        Scanner scanner = new Scanner(System.in);

        switch (command) {
            case 1:
                UserInterface.tasksHeaderPrint();
                tasks.values().forEach(this::showTask);
                epicTasks.values().forEach(et -> {
                    showTask(et);
                    et.getSubTasks().forEach(this::showTask);
                });
                break;

            case 2:
                UserInterface.tasksHeaderPrint();
                tasks.values().forEach(this::showTask);
                break;

            case 3:
                UserInterface.tasksHeaderPrint();
                epicTasks.values().forEach(this::showTask);
                break;

            case 4:
                UserInterface.tasksHeaderPrint();
                subTasks.values().forEach(this::showTask);
                break;
            case 5:
                UserInterface.tasksHeaderPrint();
                getPrioritizedTasks().forEach(this::showTask);
                break;

            case 0:
                return;
        }
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    @Override
    public void editTaskById(int taskId) {

        Scanner scanner = new Scanner(System.in);
        String taskName;
        String taskDescription;
        TaskStatus taskStatus;
        int command;

        System.out.print("Введите название: ");
        taskName = scanner.nextLine();
        if (taskName.equals("0"))
            return;
        System.out.print("Введите описание: ");
        taskDescription = scanner.nextLine();
        if (taskDescription.equals("0"))
            return;
        if (epicTasks.get(taskId) == null) {
            UserInterface.statusMenuPrint();
            System.out.print("Выберите статус задачи: ");
            command = Utils.checkCommand(TASK_STATUS_COMMAND_COUNT);
            if (command == 0)
                return;
            taskStatus = setTaskStatus(command);
        } else {
            taskStatus = setTaskStatus(1);
        }
        Task task;
        if (tasks.get(taskId) != null) {
            task = tasks.get(taskId);
            task.setTaskStatus(taskStatus);
        } else if (epicTasks.get(taskId) != null) {
            task = epicTasks.get(taskId);
        } else if (subTasks.get(taskId) != null) {
            task = subTasks.get(taskId);
            task.setTaskStatus(taskStatus);
        } else {
            System.out.println("Такой задачи нет!");
            return;
        }
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        System.out.println("Задача отредактирована");
    }

    @Override
    public  void getHistory() {
        Scanner scanner = new Scanner(System.in);
        List<Task> tempList =  taskHistory.getHistory();

        tempList.forEach(this::showTask);
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        if (!isSorted) {
            Set<Task> taskList = allTypeTasks.values().stream()
                    .filter(task -> task.getStartTime() != null)
                    .peek(taskTreeSet::add)
                    .collect(Collectors.toSet());
            System.out.println(taskList);
            isSorted = true;
        }
        return taskTreeSet;
    }

    public void createTasksScript() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        Task task = new Task("Сходить в магазин", "Купить хлеб", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        fileBackedTaskManager.save(task, TASK_BACKUP_PATH);
        Task task2 = new Task("Купить билеты в кино", "Сеанс в субботу", TaskStatus.IN_PROGRESS);
        task2.setStartTime(LocalDateTime.now().plusMinutes(120));
        task2.setDuration(Duration.ofMinutes(30));
        fileBackedTaskManager.save(task2, TASK_BACKUP_PATH);
        EpicTask epicTask = new EpicTask("Организовать день рождения", "Успеть до мая");
        fileBackedTaskManager.save(epicTask, TASK_BACKUP_PATH);
        SubTask subTask = new SubTask("Выбрать ресторан", "Кристалл", TaskStatus.NEW, epicTask);
        subTask.setStartTime(LocalDateTime.now().plusMinutes(10));
        subTask.setDuration(Duration.ofMinutes(40));
        fileBackedTaskManager.save(subTask, TASK_BACKUP_PATH);
        SubTask subTask2 = new SubTask("Купить украшения", "Шарики декор", TaskStatus.NEW, epicTask);
        subTask2.setStartTime(LocalDateTime.now().plusMinutes(240));
        subTask2.setDuration(Duration.ofMinutes(120));
        fileBackedTaskManager.save(subTask2, TASK_BACKUP_PATH);
        SubTask subTask3 = new SubTask("Список гостей", "Не определен", TaskStatus.NEW, epicTask);
        subTask3.setStartTime(LocalDateTime.now().plusMinutes(30));
        subTask3.setDuration(Duration.ofMinutes(10));
        fileBackedTaskManager.save(subTask3, TASK_BACKUP_PATH);
        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        EpicTask epicTask2 = new EpicTask("Стать разработчиком", "До конца года");
        fileBackedTaskManager.save(epicTask2, TASK_BACKUP_PATH);
        List<Task> listScript = Arrays.asList(task, task2, epicTask, subTask, subTask2, subTask3, epicTask2);

        for (Task t : listScript) {
            allTypeTasks.put(t.getTaskId(), t);
            if (t.getClass() == Task.class) {
                tasks.put(t.getTaskId(), t);
            }
            if (t.getClass() == EpicTask.class) {
                epicTasks.put(t.getTaskId(), (EpicTask) t);
            }
            if (t.getClass() == SubTask.class) {
                subTasks.put(t.getTaskId(), (SubTask) t);
            }
        }
        System.out.println("Задачи созданы");
    }
}
