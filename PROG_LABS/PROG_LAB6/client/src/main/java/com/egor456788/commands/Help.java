package com.egor456788.commands;

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
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        String output = "";
        for (String key:  commandManager.getCommands().keySet())
            output += key + "   " + commandManager.getCommands().get(key).getDescription()+"\n";
        return (T)output;
    }
}
