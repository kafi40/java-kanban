import task.*;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Задачник - \"Всё успею!\"");

        int taskId;

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
                            System.out.println("0 - Выйти");
                            TaskManager.addTask(command);
                            break;

                        case 2:
                            System.out.println("Добавить сложную");
                            System.out.println("0 - Выйти");
                            TaskManager.addTask(command);
                            break;

                        case 3:
                            System.out.println("Добавить подзадачу");
                            System.out.println("0 - Выйти");
                            TaskManager.addTask(command);
                            break;

                        case 0:
                            continue ;
                    }
                    break;

                case 2:
                    System.out.println("Выберите, что отобразить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(Parameters.SHOW_TASKS_COMMAND_COUNT);
                    TaskManager.showTasks(command);
                    break;

                case 3:
                    System.out.println("Выберите, что удалить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(Parameters.SHOW_TASKS_COMMAND_COUNT);
                    TaskManager.clearTasks(command);
                    System.out.println("Задачи удалены!");
                    break;

                case 4:
                    System.out.println("Поиск задачи по ID");
                    System.out.println("0 - Выйти");
                    System.out.print("Введите ID: ");
                    taskId = Utils.checkId(TaskManager.taskIdCounter);
                    if (taskId == 0)
                        continue;
                    TaskManager.findTaskById(taskId);
                    break;

                case 5:
                    System.out.println("Редактировать задачу по ID");
                    System.out.println("0 - Выйти");
                    System.out.print("Введите ID: ");
                    taskId = Utils.checkId(TaskManager.taskIdCounter);
                    if (taskId == 0)
                        continue;
                    TaskManager.editTaskById(taskId);
                    break;

                case 6:
                    System.out.println("Удалить задачу по ID");
                    System.out.println("0 - Выйти");
                    System.out.print("Введите ID: ");
                    taskId = Utils.checkId(TaskManager.taskIdCounter);
                    if (taskId == 0)
                        continue;
                    TaskManager.deleteTaskById();
                    break;

                case 0:
                    break OUTER;
            }
        }
    }
}
