package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;

import java.util.HashSet;

/**
 * Команда выводящая уникальные значения возраста
 */
public class PrintUniqueAge extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintUniqueAge(CollectionMeneger collectionMeneger) {
        super("print_unique_age", "Выводит уникальные значения возраста элементов");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Выводит уникальные значения возраста
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
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
