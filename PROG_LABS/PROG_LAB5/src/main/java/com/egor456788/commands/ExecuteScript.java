package com.egor456788.commands;

import com.egor456788.exceptions.Ex;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.CommandManager;
import com.egor456788.menegers.Invoker;
import com.egor456788.menegers.Printer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
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


        try {

            BufferedReader reader = new BufferedReader(new FileReader(args));
            commandManager.register("add_alter", new Add(collectionMeneger, printer, reader, true));
            commandManager.register("add_if_min_alter", new AddIfMin(collectionMeneger, printer, reader, true));
            commandManager.register("update_id_alter", new UpdateId(collectionMeneger, printer, reader, true));

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty())
                    invoker.invokeAlter(line);
            }

        } catch (Exception e) {
            recursionCheck.remove(recursionCheck.size()-1);
            return (T)(getName() + " " + args + ": ОШИБКА чтения файла " + e.getMessage());
        }
        recursionCheck.remove(recursionCheck.size()-1);
        return (T) ("Скрипт " + args + " выполнен");
    }
}
