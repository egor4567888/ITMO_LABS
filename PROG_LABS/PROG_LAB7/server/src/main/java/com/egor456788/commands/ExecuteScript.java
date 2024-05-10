package com.egor456788.commands;

import com.egor456788.Applicaton;
import com.egor456788.Creator;
import com.egor456788.Printer;
import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.CommandManager;
import com.egor456788.menegers.Invoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда исполняющая скрипт
 */
public class ExecuteScript extends Command {
    final Invoker invoker;
    final CommandManager commandManager;
    final CollectionMeneger collectionMeneger;
    final Printer printer;
    final List<String> recursionCheck = new ArrayList<>();
    int counter = 0;
    public ExecuteScript(Invoker invoker, CommandManager commandManager, CollectionMeneger collectionMeneger, Printer printer) {
        super("execute_script", "исполняет скрипт из указанного файла");
        this.invoker = invoker;
        this.commandManager = commandManager;
        this.collectionMeneger = collectionMeneger;
        this.printer = printer;
    }

    /**
     * Исполняет скрипт по введённому адресу
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args == null)
            args = "";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String currentMethod = stackTraceElements[1].getClassName() + " " + args;
        if (recursionCheck.contains(currentMethod))
            return (T) (getName() + " " + args + ": ОШИБКА рекурсия");

        recursionCheck.add(currentMethod);
        /*System.out.println("Текущий " + currentMethod);
        System.out.println(Arrays.toString(stackTraceElements));
        for (int i = 2; i < stackTraceElements.length -1;  i++) {
            System.out.println(stackTraceElements[i].toString());
            if (currentMethod.equals(stackTraceElements[i].toString())) {
                return (T) (getName() + " " + args + ": ОШИБКА рекурсия");
            }
        }
        if (counter > 3)
            return null;
        counter+=1;*/
        try {
            File f = new
                    File(args);
        }
        catch (RuntimeException e){
            recursionCheck.remove(recursionCheck.size()-1);
            return (T)(getName() + " " + args + ": ОШИБКА файл не найден");
        }

        String output = "";
        try {

            BufferedReader reader = new BufferedReader(new FileReader(args));
            commandManager.register("add_alter", new Add(collectionMeneger, printer, reader, true));
            commandManager.register("add_if_min_alter", new AddIfMin(collectionMeneger, printer, reader, true));
            commandManager.register("update_id_alter", new UpdateId(collectionMeneger, printer, reader, true));

            String line;
            String[] input;
            String comArgs = null;
            Entity entity = null;
            while ((line = reader.readLine()) != null) {
                comArgs = null;
                entity = null;
                if (!line.isEmpty()) {
                    input = line.trim().split(" ");
                    if (input.length >2)
                        output +=line + ": ОШИБКА избыточное число аргументов" + "\n";
                    else {
                        if (input.length == 2)
                            comArgs = input[1];
                        try {
                            if (Applicaton.commandsPack.getCommands().contains(input[0]))
                                entity = Creator.create(new Printer(),reader,true);
                            output += invoker.invokeAlter(new Request(input[0], comArgs, entity)) + "\n";
                        }
                        catch (Exception e){
                            output+=getName() + " " + args + ": ОШИБКА "+ e.getMessage();
                        }
                    }
                }
            }

        } catch (Exception e) {
            recursionCheck.remove(recursionCheck.size()-1);
            return (T)(getName() + " " + args + ": ОШИБКА чтения файла " + e.getMessage());
        }
        recursionCheck.remove(recursionCheck.size()-1);
        return (T) (output + "Скрипт " + args + " выполнен");
    }
}