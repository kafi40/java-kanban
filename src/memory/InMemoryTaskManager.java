package memory;

import interfaces.TaskHistory;
import interfaces.TaskManager;
import task.*;
import util.Parameters;
import util.TaskStatus;
import util.UserInterface;
import util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> allTypeTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public static int taskIdCounter = 0;

    TaskHistory taskHistory = new InMemoryTaskHistory();

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

        for(EpicTask et : epicTasks.values()) {
            if(et.getSubTasks().isEmpty()) {
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
            if(isNewTaskFlag) {
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
        System.out.print(Utils.tableFormatter(String.valueOf(task.getTaskId())));
        System.out.print(Utils.tableFormatter(task.getTaskName()));
        System.out.print(Utils.tableFormatter(task.getTaskDescription()));
        System.out.print(Utils.tableFormatter(taskStatus));
        System.out.println(Utils.tableFormatter(epicTask));
    }

    @Override
    public void addTask(int command) {

        Scanner scanner = new Scanner(System.in);
        String taskName;
        String taskDescription;
        TaskStatus taskStatus;

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
                command = Utils.checkCommand(Parameters.TASK_STATUS_COMMAND_COUNT);
                if (command == 0) return;
                taskStatus = setTaskStatus(command);
                Task task = new Task(taskName, taskDescription, taskStatus);
                tasks.put(task.getTaskId(), task);
                allTypeTasks.put(task.getTaskId(), task);
                break;

            case 2:
                EpicTask epicTask = new EpicTask(taskName, taskDescription);
                epicTasks.put(epicTask.getTaskId(), epicTask);
                allTypeTasks.put(epicTask.getTaskId(), epicTask);
                break;

            case 3:
                UserInterface.statusMenuPrint();
                System.out.print("Выберите статус задачи: ");
                command = Utils.checkCommand(Parameters.TASK_STATUS_COMMAND_COUNT);
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
                subTasks.put(subTask.getTaskId(), subTask);
                epicTasks.get(mainEpicTaskId).addSubTask(subTask);
                allTypeTasks.put(subTask.getTaskId(), subTask);
        }
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
    public void findTaskById(int taskId) {

        Scanner scanner = new Scanner(System.in);

        if (taskId == 0) return;
        Task task;
        if(tasks.get(taskId) != null) {
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
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    @Override
    public void deleteTaskById(int taskId ) {
        allTypeTasks.remove(taskId);
        if(tasks.get(taskId) != null) {
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
                for (Task t : tasks.values()) {
                    showTask(t);
                }
                for (EpicTask et : epicTasks.values()) {
                    showTask(et);
                    for (SubTask st : et.getSubTasks()) {
                        showTask(st);
                    }
                }
                break;

            case 2:
                UserInterface.tasksHeaderPrint();
                for (Task t : tasks.values()) {
                    showTask(t);
                }
                break;

            case 3:
                UserInterface.tasksHeaderPrint();
                for (EpicTask et : epicTasks.values()) {
                    showTask(et);
                }
                break;

            case 4:
                UserInterface.tasksHeaderPrint();
                for (SubTask st : subTasks.values()) {
                    showTask(st);
                }
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
        if(epicTasks.get(taskId) == null) {
            UserInterface.statusMenuPrint();
            System.out.print("Выберите статус задачи: ");
            command = Utils.checkCommand(Parameters.TASK_STATUS_COMMAND_COUNT);
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
    public void getHistory() {

        Scanner scanner = new Scanner(System.in);

        for (Task t : taskHistory.getHistory()) {
            showTask(t);
        }
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

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
}
