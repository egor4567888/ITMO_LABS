package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CommandManager;

/**
 * Команда выводящая список доступных команд
 */
public class Help extends Command{
    final CommandManager commandManager;
    public Help(CommandManager commandManager) {
        super("help", "выводит информацию о доступных командах");
        this.commandManager = commandManager;
    }

    /**
     * Выводит список доступных команд
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
        String output = "";
        for (String key:  commandManager.getCommands().keySet())
            if (!key.contains("alter")) {
                output += key + "   " + commandManager.getCommands().get(key).getDescription() + "\n";
            }
        return (T)output;
    }
}
