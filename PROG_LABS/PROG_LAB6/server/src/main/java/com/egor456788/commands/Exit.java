package com.egor456788.commands;

import com.egor456788.Request;

/**
 * Команда для завершения работы программы
 */
public class Exit extends Command{

    public Exit() {
        super("exit", "выход");
    }

    /**
     * Завершает работу программы
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        System.exit(0);
        return null;
    }
}
