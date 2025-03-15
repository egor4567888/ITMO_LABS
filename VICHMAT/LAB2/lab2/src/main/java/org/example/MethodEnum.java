package org.example;

public enum MethodEnum {
    BISECTION("Метод бисекции"),
    NEWTON("Метод Ньютона"),
    SIMPLE_ITERATION("Метод простой итерации"),
    CHORD("Метод хорд"),
    SECANT("Метод секущих");

    private final String description;

    MethodEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static MethodEnum fromChoice(int choice) {
        switch (choice) {
            case 1:
                return BISECTION;
            case 2:
                return NEWTON;
            case 3:
                return SIMPLE_ITERATION;
            case 4:
                return CHORD;
            case 5:
                return SECANT;
            default:
                throw new IllegalArgumentException("Некорректный выбор метода.");
        }
    }
}