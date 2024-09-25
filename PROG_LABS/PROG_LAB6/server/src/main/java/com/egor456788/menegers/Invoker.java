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
        String output="";
        try {
            if (commandManager.getCommands().get(request.getCommandName()) == null)
                throw new InputException("Комманда " + request.getCommandName() + " не найдена");
            String args = request.getArgs();


            output = commandManager.getCommands().get(request.getCommandName()).<String>execute(request);
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
    public String invokeAlter (Request request){
        String commandName;
        commandName = request.getCommandName();
        commandName += "_alter";
        if (commandManager.getCommands().get(commandName) == null){
            return invoke(request);

        }
        else return invoke(new Request(commandName,request.getArgs(),request.getEntity(),request.getPort()));

    }
}
