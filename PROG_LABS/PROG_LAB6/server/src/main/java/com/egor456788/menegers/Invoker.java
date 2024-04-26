package com.egor456788.menegers;

import com.egor456788.Request;
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
    public String invoke(Request request){

        try {
            if (commandManager.getCommands().get(request.getCommandName()) == null)
                throw new InputException("Комманда " + request.getCommandName() + " не найдена");
            String args = request.getArgs();


            String output = commandManager.getCommands().get(request.getCommandName()).<String>execute(request);
            OutputBlocker.unblockOutput();
            String toHistory = request.getCommandName();
            if (request.getArgs() != null)
                toHistory += " " + args;
            if (!(request.getEntity()==null))
                toHistory +=" " + request.getEntity();
            commandManager.addToHistory(toHistory);
            return output;
        }
        catch (InputException e) {
            return ("ОШИБКА " + e.getMessage());
        }

    }
    /*public void invokeAlter (String input){
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
    }*/
}
