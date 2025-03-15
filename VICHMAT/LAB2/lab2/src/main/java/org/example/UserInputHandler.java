package org.example;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.example.Methods.MC;

@Component
public class UserInputHandler {

    public UserInputData processInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите источник ввода: 1 - консоль, 2 - файл:");
        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            return processConsoleInput(scanner);
        } else if (choice.equals("2")) {
            return processFileInput(scanner);
        } else {
            throw new IllegalArgumentException("Некорректный выбор источника ввода. Введите 1 или 2.");
        }
    }

    private UserInputData processConsoleInput(Scanner scanner) {
        try {
            System.out.println("Выберите функцию для нахождения корней:");
            System.out.println("1: " + MathFunction.FIRST.getDescription());
            System.out.println("2: " + MathFunction.SECOND.getDescription());
            System.out.println("3: " + MathFunction.THIRD.getDescription());
            int funcChoice = Integer.parseInt(scanner.nextLine().trim());
            MathFunction chosenFunction = MathFunction.fromChoice(funcChoice);

            System.out.println("Выберите метод решения:");
            System.out.println("1: " + MethodEnum.BISECTION.getDescription());
            System.out.println("2: " + MethodEnum.NEWTON.getDescription());
            System.out.println("3: " + MethodEnum.SIMPLE_ITERATION.getDescription());
            System.out.println("4: " + MethodEnum.CHORD.getDescription());
            System.out.println("5: " + MethodEnum.SECANT.getDescription());
            int methodChoice = Integer.parseInt(scanner.nextLine().trim());
            MethodEnum chosenMethod = MethodEnum.fromChoice(methodChoice);

            System.out.println("Введите начало интервала:");
            BigDecimal a = new BigDecimal(scanner.nextLine().trim());
            System.out.println("Введите конец интервала:");
            BigDecimal b = new BigDecimal(scanner.nextLine().trim());
            validateInterval(a, b, chosenFunction);

            BigDecimal x0 = null, x1 = null;
            if (chosenMethod == MethodEnum.NEWTON) {
                System.out.println("Введите начальное приближение x0:");
                x0 = new BigDecimal(scanner.nextLine().trim());
            } else if (chosenMethod == MethodEnum.SIMPLE_ITERATION) {
                x0 = a.add(b).divide(new BigDecimal("2"));
            } else if (chosenMethod == MethodEnum.SECANT) {
                System.out.println("Введите первое приближение x0:");
                x0 = new BigDecimal(scanner.nextLine().trim());
                System.out.println("Введите второе приближение x1:");
                x1 = new BigDecimal(scanner.nextLine().trim());
            }


            System.out.println("Введите допустимую погрешность:");
            BigDecimal epsilon = new BigDecimal(scanner.nextLine().trim());

            System.out.println("Введены следующие параметры:");
            System.out.println("Функция: " + chosenFunction.getDescription());
            System.out.println("Метод: " + chosenMethod.getDescription());
            if (a != null && b != null) {
                System.out.printf("Интервал: [%s, %s]%n", a.toPlainString(), b.toPlainString());
            }
            if (x0 != null) {
                System.out.println("Начальное приближение x0: " + x0.toPlainString());
            }
            if (x1 != null) {
                System.out.println("Второе приближение x1: " + x1.toPlainString());
            }
            System.out.println("Погрешность: " + epsilon.toPlainString());

            return new UserInputData(chosenFunction, chosenMethod, a, b, x0, x1, epsilon);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Неверный формат числа!", ex);
        }
    }

    private UserInputData processFileInput(Scanner scanner) {
        System.out.println("Введите имя файла:");
        String filename = scanner.nextLine().trim();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            int funcChoice = Integer.parseInt(fileScanner.nextLine().trim());
            MathFunction chosenFunction = MathFunction.fromChoice(funcChoice);

            int methodChoice = Integer.parseInt(fileScanner.nextLine().trim());
            MethodEnum chosenMethod = MethodEnum.fromChoice(methodChoice);

            BigDecimal a = null, b = null, x0 = null, x1 = null;
            if (chosenMethod == MethodEnum.BISECTION || chosenMethod == MethodEnum.CHORD) {
                String[] intervalParts = fileScanner.nextLine().trim().split("\\s+");
                if (intervalParts.length < 2) {
                    throw new IllegalArgumentException("Интервал должен содержать 2 числа, разделённых пробелом.");
                }
                a = new BigDecimal(intervalParts[0]);
                b = new BigDecimal(intervalParts[1]);
                validateInterval(a, b, chosenFunction);
            } else if (chosenMethod == MethodEnum.NEWTON) {
                x0 = new BigDecimal(fileScanner.nextLine().trim());
            } else if (chosenMethod == MethodEnum.SIMPLE_ITERATION) {
                String[] intervalParts = fileScanner.nextLine().trim().split("\\s+");
                if (intervalParts.length < 2) {
                    throw new IllegalArgumentException("Интервал должен содержать 2 числа, разделённых пробелом.");
                }
                a = new BigDecimal(intervalParts[0]);
                b = new BigDecimal(intervalParts[1]);
                validateInterval(a, b, chosenFunction);
                x0 = a.add(b).divide(new BigDecimal("2"));
            } else if (chosenMethod == MethodEnum.SECANT) {
                String[] initialParts = fileScanner.nextLine().trim().split("\\s+");
                if (initialParts.length < 2) {
                    throw new IllegalArgumentException("Для метода секущих необходимо указать 2 приближения x0 и x1.");
                }
                x0 = new BigDecimal(initialParts[0]);
                x1 = new BigDecimal(initialParts[1]);
            }

            BigDecimal epsilon = new BigDecimal(fileScanner.nextLine().trim());

            System.out.println("Введены параметры из файла:");
            System.out.println("Функция: " + chosenFunction.getDescription());
            System.out.println("Метод: " + chosenMethod.getDescription());
            if (a != null && b != null) {
                System.out.printf("Интервал: [%s, %s]%n", a.toPlainString(), b.toPlainString());
            }
            if (x0 != null) {
                System.out.println("Начальное приближение x0: " + x0.toPlainString());
            }
            if (x1 != null) {
                System.out.println("Второе приближение x1: " + x1.toPlainString());
            }
            System.out.println("Погрешность: " + epsilon.toPlainString());

            return new UserInputData(chosenFunction, chosenMethod, a, b, x0, x1, epsilon);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден: " + filename, e);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Неверный формат числа в файле!", ex);
        }
    }

    static void validateInterval(BigDecimal a, BigDecimal b, MathFunction function) {
        if (a.compareTo(b) >= 0) {
            throw new IllegalArgumentException("Начало интервала должно быть меньше конца.");
        }
        BigDecimal fa = function.getFunction().apply(a);
        BigDecimal fb = function.getFunction().apply(b);
        if (fa.multiply(fb).compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException("Не выполнено необходимое условие существования корня (f(a) * f(b) должно быть < 0).");
        }
        BigDecimal step = BigDecimal.ONE.divide(new BigDecimal("100"), MC);
        BigDecimal current = a;
        BigDecimal previousDerivative = function.getDerivative().apply(current);
        int previousSign = previousDerivative.signum();

        for (int i = 1; i <= (b.doubleValue()-a.doubleValue())*100; i++) {
            current = current.add(step, MC);
            BigDecimal currentDerivative = function.getDerivative().apply(current);

            int currentSign = currentDerivative.signum();
            if (currentSign != previousSign) {
                throw new IllegalArgumentException("Не выполнено достаточное условие единственности корня (производная должна сохранять знак на отрезке).");
            }
            previousSign = currentSign;
        }
    }
}