package util;

import java.util.Scanner;

public class Utils {

    public static int checkCommand(int commandCount) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int command;
            if(!scanner.hasNextInt()) {
                System.out.println("Такой команды нет!");
            } else {
                command = scanner.nextInt();
                if (command > commandCount - 1 || command < 0) {
                    System.out.println("Такой команды нет!");
                } else {
                    return command;
                }
            }
        }
    }
}
