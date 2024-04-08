package com.egor456788.commands;

import com.egor456788.menegers.CommandManager;

public class Help extends Command{
    final CommandManager commandManager;
    public Help(CommandManager commandManager) {
        super("help", "выводит информацию о доступных командах");
        this.commandManager = commandManager;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");
        String output = "";
        for (String key:  commandManager.getCommands().keySet())
            output += key + "   " + commandManager.getCommands().get(key).getDescription()+"\n";
        return (T)output;
    }
}
