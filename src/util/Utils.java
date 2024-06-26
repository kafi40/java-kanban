package util;

import exceptions.InputDurationException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

import static util.Parameters.DTF;

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

    public static int checkId() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int inputId;
            if (!scanner.hasNextInt()) {
                System.out.print("Некорректный ID! Попробуйте снова: ");
            } else {
                inputId = scanner.nextInt();
                if (inputId < 0) {
                    System.out.print("Некорректный ID! Попробуйте снова: ");
                } else {
                    return inputId;
                }
            }
        }
    }

    public static Optional<Integer> checkDuration() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                Optional<Integer> inputDuration;
                if (!scanner.hasNextInt()) {
                    throw new InputDurationException("Вы ввели строку или дробь: ", scanner.next());
                } else {
                    inputDuration = Optional.of(scanner.nextInt());
                    if (inputDuration.get() < 0) {
                        throw new InputDurationException("Вы ввели отрицательное число: ", inputDuration.get());
                    } else {
                        return inputDuration;
                    }
                }
            } catch (InputDurationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static LocalDateTime localDateTimeFormatter() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            LocalDateTime localDateTime;
            String input = scanner.nextLine();
            try {
                if (input.isBlank()) {
                    return null;
                }
                localDateTime = LocalDateTime.parse(input, DTF);
            } catch (DateTimeException e) {
                System.out.println("Неверный формат даты и времени");
                continue;
            }
            return localDateTime;
        }
    }
}
