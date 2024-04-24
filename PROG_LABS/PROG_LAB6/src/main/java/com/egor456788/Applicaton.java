package com.egor456788;

import com.egor456788.commands.*;
import com.egor456788.exceptions.WrongPathException;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.CommandManager;
import com.egor456788.menegers.Invoker;
import com.egor456788.menegers.Printer;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.util.Scanner;

public class Applicaton {
    public void run(String[] args) {
        Printer printer = new Printer();
        CommandManager commandManager = new CommandManager();
        CollectionMeneger collectionMeneger = new CollectionMeneger();
        collectionMeneger.addFromFile(args[0]);
        Invoker invoker = new Invoker(commandManager);
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        commandManager.register("info", new Info(collectionMeneger));
        commandManager.register("add", new Add(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("show", new Show(collectionMeneger));
        commandManager.register("exit",new Exit());
        commandManager.register("history",new History(commandManager));
        commandManager.register("add_if_min", new AddIfMin(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("help", new Help(commandManager));
        commandManager.register("clear", new Clear(collectionMeneger));
        commandManager.register("remove_by_id",new RemoveById(collectionMeneger));
        commandManager.register("update_id", new UpdateId(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("save", new Save(collectionMeneger));
        commandManager.register("remove_lower", new RemoveLower(collectionMeneger));
        commandManager.register("filter_by_age", new FilterByAge(collectionMeneger));
        commandManager.register("print_field_ascending_name",new PrintFieldAscendingName(collectionMeneger));
        commandManager.register("print_unique_age", new PrintUniqueAge(collectionMeneger));
        commandManager.register("execute_script", new ExecuteScript(invoker,commandManager,collectionMeneger,printer));
        commandManager.register("print_name_by_id",new PrintNameById(collectionMeneger));

        printer.println("Введите help для вывода информации о командах");
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                invoker.invoke(input);

            }
            //test
            System.exit(0);

        }
    }
}
