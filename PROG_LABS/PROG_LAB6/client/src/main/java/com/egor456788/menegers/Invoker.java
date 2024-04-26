package com.egor456788.menegers;

import com.egor456788.exceptions.InputException;

/**
 * Класс для вызова метода execute у команд
 */

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

            String output = commandManager.getCommands().get(command[0]).<String>execute(args);
            OutputBlocker.unblockOutput();
            printer.println(output);
            commandManager.addToHistory(command[0]);
        }
        catch (InputException e) {
            printer.printErr(e.getMessage());
        }

    }
    public void invokeAlter (String input){
        String[] command = input.trim().split("\s+");
        try {
            String args = null;
            if (command.length > 2)
                throw new InputException("избыточное число аргументов");
            if (command.length == 2)
                args = command[1];
            command[0] += "_alter";
            if (commandManager.getCommands().get(command[0]) == null) {
                invoke(input);
                return;
            }
            String output = commandManager.getCommands().get(command[0]).<String>execute(args);
            OutputBlocker.unblockOutput();
            printer.println(output);
            commandManager.addToHistory(command[0]);
        }
        catch (InputException e) {
            printer.printErr(e.getMessage());
        }
    }
}
