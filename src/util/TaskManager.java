package util;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {

    public static HashMap<Integer, Object> allTypeTasks = new HashMap<>();
    public static HashMap<Integer, Task> tasks = new HashMap<>();
    public static HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    public static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public static int taskIdCounter = 0;

    public static int taskIdGenerator() {
       taskIdCounter = taskIdCounter + 1;
       return taskIdCounter;
    }

    public static TaskStatus setTaskStatus(int command) {
        return switch (command) {
            case 1 -> TaskStatus.NEW;
            case 2 -> TaskStatus.IN_PROGRESS;
            case 3 -> TaskStatus.DONE;
            default -> TaskStatus.NEW;
        };
    }

    public static void setEpicTaskStatus(EpicTask epicTask) {
        boolean isNewTaskFlag = true;

        for (SubTask st : epicTask.getSubTasks()) {
            if (st.getTaskStatus() == TaskStatus.DONE || )
        }
    }

    public static void showTask(Task task) {
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

    public static void addTask(int command) {

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
                taskStatus = TaskManager.setTaskStatus(command);
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
                taskStatus = TaskManager.setTaskStatus(command);
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
                allTypeTasks.put(subTask.getTaskId(), subTask);
        }
        System.out.println("Задача добавлена!");
    }

    public static void clearTasks(int command) {
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

    public static void findTaskById(int taskId) {

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
        TaskManager.showTask(task);
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    public static void deleteTaskById() {
        System.out.print("Введите ID: ");
        int taskId = Utils.checkId(taskIdCounter);
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

    public static void showTasks(int command) {

        Scanner scanner = new Scanner(System.in);

        switch (command) {
            case 1:
                UserInterface.tasksHeaderPrint();
                for (Task t : tasks.values()) {
                    TaskManager.showTask(t);
                }
                for (EpicTask et : epicTasks.values()) {
                    TaskManager.showTask(et);
                    for (SubTask st : subTasks.values()) {
                        TaskManager.showTask(st);
                    }
                }
                break;

            case 2:
                UserInterface.tasksHeaderPrint();
                for (Task t : tasks.values()) {
                    TaskManager.showTask(t);
                }
                break;

            case 3:
                UserInterface.tasksHeaderPrint();
                for (EpicTask et : epicTasks.values()) {
                    TaskManager.showTask(et);
                }
                break;

            case 4:
                UserInterface.tasksHeaderPrint();
                for (SubTask st : subTasks.values()) {
                    TaskManager.showTask(st);
                }
                break;

            case 0:
                return;
        }
        System.out.print("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    public static void editTaskById(int taskId) {

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
}
