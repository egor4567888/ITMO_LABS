package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Comparator;

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


        String output = "";
        int i = 0;
        Comparator<Entity> nameComparator = Comparator.comparing(Entity::getName);
        for (Entity entity: collectionMeneger.getCollection().stream().sorted(nameComparator).toList())
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
