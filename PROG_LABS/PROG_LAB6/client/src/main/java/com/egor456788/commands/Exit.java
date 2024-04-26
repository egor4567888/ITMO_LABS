package com.egor456788.commands;

/**
 * Команда для завершения работы программы
 */
public class Exit extends Command{

    public Exit() {
        super("exit", "выход");
    }

    /**
     * Завершает работу программы
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        System.exit(0);
        return null;
    }
}
