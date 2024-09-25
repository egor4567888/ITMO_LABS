package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

/**
 * Команда для вывода элементов коллекции отсортировав по возрасту
 */
public class FilterByAge extends Command{
    final CollectionMeneger collectionMeneger;
    public FilterByAge(CollectionMeneger collectionMeneger) {
        super("filter_by_age", "выводит элементы с данным возрастом");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Выводит отсортированные по возрасту элементы коллекции
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
        String output = "";
        Collections.sort(collectionMeneger.getCollection());
        try {
            int age = Integer.parseInt(args);
            for (int i = 0; i < collectionMeneger.getCollection().size(); i++) {
                if (collectionMeneger.getCollection().get(i).getAge() == age)
                    output += collectionMeneger.getCollection().get(i) + "\n";
                if (collectionMeneger.getCollection().get(i).getAge() > age)
                    break;
            }
        }
        catch (RuntimeException e){
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат возраста");
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output.length() < 2)
            return (T) "Элементов с данным возрастом не найдено";
        return (T) output;
    }
}
