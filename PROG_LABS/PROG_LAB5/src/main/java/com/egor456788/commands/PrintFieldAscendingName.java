package com.egor456788.commands;

import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

public class PrintFieldAscendingName extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintFieldAscendingName(CollectionMeneger collectionMeneger) {
        super("printFieldAscendingName", "Выводит имена элементов в порядке возрастания");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");

        Collections.sort(collectionMeneger.getCollection());
        String output = "";
        for (Entity entity: collectionMeneger.getCollection())
        {
            output += entity.getName() + "\n";
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "Коллекция пуста";
        return (T)output;
    }
}
