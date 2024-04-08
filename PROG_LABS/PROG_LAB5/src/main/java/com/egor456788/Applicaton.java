package com.egor456788;

import com.egor456788.commands.*;
import com.egor456788.exceptions.WrongPathException;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.CommandManager;
import com.egor456788.menegers.Invoker;
import com.egor456788.menegers.Printer;

import java.io.File;
import java.util.Scanner;

public class Applicaton {
    public void run(String[] args) {
        Printer printer = new Printer();
        CommandManager commandManager = new CommandManager();
        CollectionMeneger collectionMeneger = new CollectionMeneger();
        collectionMeneger.addFromFile(args[0]);
        Invoker invoker = new Invoker(commandManager);
        commandManager.register("info", new Info(collectionMeneger));
        commandManager.register("add", new Add(collectionMeneger,printer));
        commandManager.register("show", new Show(collectionMeneger));
        commandManager.register("exit",new Exit());
        commandManager.register("history",new History(commandManager));
        commandManager.register("addIfMin", new AddIfMin(collectionMeneger,printer));
        commandManager.register("help", new Help(commandManager));
        commandManager.register("clear", new Clear(collectionMeneger));
        commandManager.register("removeById",new RemoveById(collectionMeneger));
        commandManager.register("updateId", new UpdateId(collectionMeneger,printer));
        commandManager.register("save", new Save(collectionMeneger));
        commandManager.register("removeLower", new RemoveLower(collectionMeneger));
        commandManager.register("filterByAge", new FilterByAge(collectionMeneger));
        commandManager.register("printFieldAscendingName",new PrintFieldAscendingName(collectionMeneger));
        commandManager.register("printUniqueAge", new PrintUniqueAge(collectionMeneger));
        commandManager.register("executeScript", new ExecuteScript(invoker));
        printer.println("Введите help для вывода информации о командах");
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
                invoker.invoke(input);
                /*printer.println("Хотите продолжить? y/n");
                String line = "";
                do {
                    line = scanner.nextLine();
                    if (line.equals("y")) {
                        printer.println("Ведите команду");
                        break;
                    }
                    if (line.equals("n")) {
                        flag = false;
                        break;
                    }
                    printer.println("Ведите y или n");
                }while (!(line.equals("y")) && (!(line.equals("n"))));*/


            }


        }
    }
}
