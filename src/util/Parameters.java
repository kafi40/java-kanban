package util;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class Parameters {
    public static final int MAIN_MENU_COMMAND_COUNT = 9;
    public static final int ADD_MENU_COMMAND_COUNT = 4;
    public static final int TASK_STATUS_COMMAND_COUNT = 4;
    public static final int SHOW_TASKS_COMMAND_COUNT = 6;
    public static final Path TASK_BACKUP_PATH = Path.of("src/resource", "backup.txt");
    public static final Path TASK_HISTORY_BACKUP_PATH = Path.of("src/resource", "history.txt");
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

}
