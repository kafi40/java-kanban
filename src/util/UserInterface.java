package util;

public class UserInterface {

    public static void mainMenuPrint() {
        System.out.println("Введите команду:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Отобразить задачи");
        System.out.println("3 - Удалить все задачи");
        System.out.println("4 - Отобразить задачу по ID");
        System.out.println("5 - Изменить задачу по ID");
        System.out.println("6 - Удалить задачу по ID");
        System.out.println("7 - История просмотра");
        System.out.println("8 - Сценарий задач");
        System.out.println("0 - Выйти");
    }

    public static void addTaskMenuPrint() {
        System.out.println("Введите команду:");
        System.out.println("1 - Обычная задача");
        System.out.println("2 - Сложная задача");
        System.out.println("3 - Подзадача");
        System.out.println("0 - Назад");

    }

    public static void statusMenuPrint() {
        System.out.println("1 - Новая");
        System.out.println("2 - В процессе");
        System.out.println("3 - Завершенная");
    }

    public static void showTasksMenuPrint() {
        System.out.println("Введите команду:");
        System.out.println("1 - Все задачи");
        System.out.println("2 - Обычные задачи");
        System.out.println("3 - Сложные задачи");
        System.out.println("4 - Подзадачи");
        System.out.println("5 - Отсортированные задача");
        System.out.println("0 - Назад");

    }

    public static void tasksHeaderPrint() {
        System.out.printf("%-10s %-30s %-30s %-15s %-30s %-20s %-20s\n",
                "ID", "Название", "Описание", "Статус", "Основная задача", "Начало", "Конец");
    }
}
