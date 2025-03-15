package org.example;

public class RootFinder {

    /*
     * На основе введённых пользователем данных (UserInputData)
     * выбирает соответствующий метод для нахождения корня.
     * Теперь в методы передаётся объект UserInputData.
     */
    public static MethodResult solve(UserInputData input) {
        switch (input.getMethod()) {
            case BISECTION:
                return Methods.bisectionMethod(input);
            case CHORD:
                return Methods.chordMethod(input);
            case NEWTON:
                return Methods.newtonMethod(input);
            case SECANT:
                return Methods.secantMethod(input);
            case SIMPLE_ITERATION:
                return Methods.simpleIteration(input);
            default:
                throw new IllegalArgumentException("Неизвестный метод решения.");
        }
    }
    public static SystemMethodResult solveSystem(SystemInputData input) {
        return Methods.simpleIterationSystem(input);
    }
}