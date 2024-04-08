package com.egor456788.commands;

import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

public class Show extends Command{

    final CollectionMeneger collectionMeneger;
    public Show(CollectionMeneger collectionMeneger) {
        super("Show", "Выводит элементы коллекции");
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
                output += entity + "\n";
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "Коллекция пуста";
        return (T)output;
    }
}
