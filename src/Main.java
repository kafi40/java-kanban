import interfaces.TaskHistory;
import io.FileBackedTaskManager;
import memory.InMemoryTaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.UserInterface;
import util.Utils;
import java.io.IOException;
import java.util.List;
import static util.Parameters.*;

public class Main {

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        TaskHistory taskHistory = inMemoryTaskManager.getTaskHistory();
        int taskId;
        List<Task> taskList = fileBackedTaskManager.getData(TASK_BACKUP_PATH);
        List<Task> taskHistoryList = fileBackedTaskManager.getData(TASK_HISTORY_BACKUP_PATH);

        taskList.forEach(t -> {
            inMemoryTaskManager.getAllTypeTasks().put(t.getTaskId(), t);
            if (t.getClass() == Task.class) {
                inMemoryTaskManager.getTasks().put(t.getTaskId(), t);
            }
            if (t.getClass() == EpicTask.class) {
                inMemoryTaskManager.getEpicTasks().put(t.getTaskId(), (EpicTask) t);
            }
            if (t.getClass() == SubTask.class) {
                inMemoryTaskManager.getSubTasks().put(t.getTaskId(), (SubTask) t);
            }
        });
        inMemoryTaskManager.getPrioritizedTasks();
        taskHistoryList.forEach(taskHistory::addTaskInHistory);

            OUTER: while (true) {
            inMemoryTaskManager.getEpicTasks().values().forEach(inMemoryTaskManager::setEpicTaskStatus);
            UserInterface.mainMenuPrint();
            int command = Utils.checkCommand(MAIN_MENU_COMMAND_COUNT);
            switch (command) {
                case 1:
                    UserInterface.addTaskMenuPrint();
                    command = Utils.checkCommand(ADD_MENU_COMMAND_COUNT);
                    switch (command) {
                        case 1:
                            System.out.println("Добавить обычную задачу");
                            System.out.println("0 - Выйти");
                            inMemoryTaskManager.addTask(command);
                            break;

                        case 2:
                            System.out.println("Добавить сложную");
                            System.out.println("0 - Выйти");
                            inMemoryTaskManager.addTask(command);
                            break;

                        case 3:
                            System.out.println("Добавить подзадачу");
                            System.out.println("0 - Выйти");
                            inMemoryTaskManager.addTask(command);
                            break;

                        case 0:
                            continue;
                    }
                    break;

                case 2:
                    System.out.println("Выберите, что отобразить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(SHOW_TASKS_COMMAND_COUNT);
                    inMemoryTaskManager.showTasks(command);
                    break;

                case 3:
                    System.out.println("Выберите, что удалить");
                    UserInterface.showTasksMenuPrint();
                    command = Utils.checkCommand(SHOW_TASKS_COMMAND_COUNT);
                    inMemoryTaskManager.clearTasks(command);
                    System.out.println("Задачи удалены!");
                    break;

                case 4:
                    System.out.println("Поиск задачи по ID");
                    System.out.println("0 - Выйти");
                    System.out.print("Введите ID: ");
                    taskId = Utils.checkId();
                    if (taskId == 0)
                        continue;
                    inMemoryTaskManager.findTaskById(taskId);
                    break;

                case 5:
                        System.out.println("Редактировать задачу по ID");
                        System.out.println("0 - Выйти");
                        System.out.print("Введите ID: ");
                    while (true) {
                        taskId = Utils.checkId();
                        if (taskId == 0)
                            continue OUTER;
                        if (inMemoryTaskManager.getAllTypeTasks().containsKey(taskId)) {
                            inMemoryTaskManager.editTaskById(taskId);
                            break;
                        } else {
                            System.out.print("Такой задачи нет! Попробуйте снова: ");
                        }
                    }
                    break;

                case 6:
                    System.out.println("Удалить задачу по ID");
                    System.out.println("0 - Выйти");
                    System.out.print("Введите ID: ");
                    taskId = Utils.checkId();
                    if (taskId == 0)
                        continue;
                    inMemoryTaskManager.deleteTaskById(taskId);
                    break;

                case 7:
                    System.out.println("История просмотра:");
                    inMemoryTaskManager.getHistory();
                    break;
                case 8:
                    inMemoryTaskManager.createTasksScript();
                    break;
                case 0:
                    break OUTER;
            }
        }
    }
}
