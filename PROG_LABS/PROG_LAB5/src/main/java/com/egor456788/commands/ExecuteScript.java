package com.egor456788.commands;

import com.egor456788.exceptions.Ex;
import com.egor456788.menegers.Invoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteScript extends Command {
    final Invoker invoker;
    public ExecuteScript(Invoker invoker) {
        super("executeScript", "исполняет скрипт из указанного файла");
        this.invoker = invoker;
    }

    @Override
    public <T> T execute(String args) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length - 1; i++){
            if (stackTraceElements[i].toString() == stackTraceElements[stackTraceElements.length - 1].toString())
                return (T)(getName() + " " + args + ": ОШИБКА рекурсия");
        }
        try {
            File f = new
                    File(args);
        }
        catch (RuntimeException e){
            return (T)(getName() + " " + args + ": ОШИБКА файл не найден");
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(args))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if






                if (!line.isEmpty())
                    invoker.invoke(line);
            }

        } catch (Exception e) {
            return (T)(getName() + " " + args + ": ОШИБКА чтения файла" + e.getMessage());
        }
        return (T) ("Скрипт " + args + " выполнен");
    }
}
