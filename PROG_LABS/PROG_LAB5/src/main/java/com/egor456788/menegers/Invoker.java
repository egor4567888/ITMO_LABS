package com.egor456788.menegers;

import com.egor456788.exceptions.InputException;
import com.egor456788.menegers.CommandManager;
import com.thoughtworks.xstream.mapper.Mapper;

public class Invoker {
    CommandManager commandManager;
    Printer printer = new Printer();
    public Invoker(CommandManager commandManager){
        this.commandManager = commandManager;
    }
    public void invoke(String input){
        String[] command = input.trim().split("\s+");
        try {
            if (commandManager.getCommands().get(command[0]) == null)
                throw new InputException("Комманда " + command[0] + " не найдена");
            String args = null;
            if (command.length > 2)
                throw new InputException("избыточное число аргументов");
            if (command.length == 2)
                args = command[1];
            printer.println(commandManager.getCommands().get(command[0]).<String>execute(args));
            commandManager.addToHistory(command[0]);
        }
        catch (InputException e) {
            printer.printErr(e.getMessage());
        }

    }
}
