package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

import java.util.HashSet;
import java.util.Set;

public class PrintUniqueAge extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintUniqueAge(CollectionMeneger collectionMeneger) {
        super("printUniqueAge", "Выводит уникальные значения возраста элементов");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");
        HashSet<Integer> ages = new HashSet<Integer>();
        String output = "";
        for (int i = 0; i<collectionMeneger.getCollection().size(); i++)
            ages.add(collectionMeneger.getCollection().get(i).getAge());
        for (int age:ages)
            output += age + "\n";
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "Коллекция пуста";
        return (T) output;
    }
}
