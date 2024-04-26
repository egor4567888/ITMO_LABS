package com.egor456788.commands;

import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

/**
 * Команда выводящая все элементы коллекции
 */
public class Show extends Command{

    final CollectionMeneger collectionMeneger;
    public Show(CollectionMeneger collectionMeneger) {
        super("Show", "Выводит элементы коллекции");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Выводит элементы коллекции
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";

        Collections.sort(collectionMeneger.getCollection());
        String output = "";
        int i = 0;
        for (Entity entity: collectionMeneger.getCollection())
        {
                output += i + " " + entity + "\n";
                i++;
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "Коллекция пуста";
        return (T)output;
    }
}
