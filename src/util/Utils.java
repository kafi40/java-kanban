package util;

import java.util.Scanner;

public class Utils {

    public static int checkCommand(int commandCount) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int command;
            if (!scanner.hasNextInt()) {
                System.out.print("Такой команды нет! Попробуйте снова: ");
            } else {
                command = scanner.nextInt();
                if (command > commandCount - 1 || command < 0) {
                    System.out.print("Такой команды нет! Попробуйте снова: ");
                } else {
                    return command;
                }
            }
        }
    }

    public static int checkId(int idCount) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int inputId;
            if (!scanner.hasNextInt()) {
                System.out.print("Такого ID нет! Попробуйте снова: ");
            } else {
                inputId = scanner.nextInt();
                if (inputId > idCount || idCount < 0) {
                    System.out.print("Такого ID нет! Попробуйте снова: ");
                } else {
                    return inputId;
                }
            }
        }
    }

    public static String tableFormatter(String s) {
        for (int i = s.length(); i < 30; i++) {
            s = s + " ";
        }
        return s;
    }
}
