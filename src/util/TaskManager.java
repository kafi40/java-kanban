package util;

import task.Task;

public class TaskManager {

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

    public static void showTask(Task task) {
        String taskStatus;
        System.out.println("ID - " + task.getTaskId());
        System.out.println("Название - " + task.getTaskName());
        System.out.println("Описание - " + task.getTaskDescription());
        taskStatus = switch (task.getTaskStatus()) {
            case NEW -> "Новая";
            case IN_PROGRESS -> "В процессе";
            case DONE -> "Выполнена";
        };
        System.out.println("Статус - " + taskStatus);
    }

}
