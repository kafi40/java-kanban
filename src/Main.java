import task.*;
import util.*;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Задачник - \"Всё успею!\"");

        HashMap<Integer, Task> tasks = new HashMap<>();
        HashMap<Integer, EpicTask> EpicTasks = new HashMap<>();
        HashMap<Integer, SubTask> SubTasks = new HashMap<>();
        String taskName;
        String taskDescription;
        TaskStatus taskStatus;
        Scanner scanner = new Scanner(System.in);

        OUTER: while (true) {

            UserInterface.mainMenuPrint();

            int command = Utils.checkCommand(Parameters.MAIN_MENU_COMMAND_COUNT);

            switch (command) {
                case 1:
                    UserInterface.addTaskMenuPrint();
                    command = Utils.checkCommand(Parameters.ADD_MENU_COMMAND_COUNT);
                    switch (command) {
                        case 1:
                            System.out.println("Добавить обычную задачу");
                            System.out.println("Введите название:");
                            taskName = scanner.nextLine();
                            System.out.println("Введите описание:");
                            taskDescription = scanner.nextLine();
                            System.out.println("Выберите статус задачи:");
                            UserInterface.statusMenuPrint();
                            command = Utils.checkCommand(Parameters.TASK_STATUS_COMMAND_COUNT);
                            taskStatus = TaskManager.setTaskStatus(command);
                            Task task = new Task(taskName, taskDescription, taskStatus);
                            tasks.put(task.getTaskId(), task);
                            break;
                        case 2:
                            System.out.println("Добавить сложную");
                            System.out.println("Введите название:");
                            taskName = scanner.nextLine();
                            System.out.println("Введите описание:");
                            taskDescription = scanner.nextLine();
                            EpicTask epicTask = new EpicTask(taskName, taskDescription);
                            tasks.put(epicTask.getTaskId(), epicTask);
                            break;
                        case 3:
                            System.out.println("Добавить подзадачу");
                            System.out.println("Введите название:");
                            taskName = scanner.nextLine();
                            System.out.println("Введите описание:");
                            taskDescription = scanner.nextLine();
                            System.out.println("Выберите основную задачу:");
                            System.out.println("Выберите статус задачи:");
                            UserInterface.statusMenuPrint();
                            command = Utils.checkCommand(Parameters.TASK_STATUS_COMMAND_COUNT);
                            taskStatus = TaskManager.setTaskStatus(command);
                            SubTask subTask = new SubTask(taskName, taskDescription, taskStatus);
                            tasks.put(subTask.getTaskId(), subTask);
                            break;
                        case 0:
                            continue ;
                    }
                    System.out.println("Задача добавлена!");
                    break;

                case 2:
                    System.out.println("Выберите, что отобразить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(Parameters.SHOW_TASKS_COMMAND_COUNT);
                    switch (command) {
                        case 1:
                            for (Task task : tasks.values()) {
                                TaskManager.showTask(task);
                            }
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 0:
                            continue;
                    }
                    break;

                case 3:
                    System.out.println("Выберите, что удалить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(Parameters.SHOW_TASKS_COMMAND_COUNT);
                    switch (command) {
                        case 1:
                            tasks.clear();
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 0:
                            continue ;
                    }
                    System.out.println("Задачи удалены!");
                    break;

                case 4:
                    System.out.println("Поиск задачи по ID");
                    System.out.print("Введите ID: ");
                    int taskId = scanner.nextInt();
                    Task task = tasks.get(taskId);
                    TaskManager.showTask(task);
                    break;
                case 5:
                    System.out.println("Редактировать задачу по ID");
                    System.out.print("Введите ID: ");
                    taskId = scanner.nextInt();
                    task = tasks.get(taskId);
                    System.out.println("Задача отредактирована");
                    break;
                case 6:
                    System.out.println("Удалить задачу по ID");
                    System.out.print("Введите ID: ");
                    taskId = scanner.nextInt();
                    tasks.remove(taskId);
                    System.out.println("Задача удалена");
                    break;
                case 0:
                    break OUTER;
            }
        }
    }
}
