package util;

import exceptions.InputDurationException;

import java.util.Optional;
import java.util.Scanner;

public class Utils {

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
}
