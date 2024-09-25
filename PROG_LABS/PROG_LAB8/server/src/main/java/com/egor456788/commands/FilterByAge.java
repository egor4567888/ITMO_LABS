package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Comparator;
import java.util.stream.Collectors;

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
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        String output = "";
        try {
            int age = Integer.parseInt(args);

            Comparator<Entity> nameComparator = Comparator.comparing(Entity::getName);
            for(Entity entity: collectionMeneger.getCollection().stream().filter(entity -> entity.getAge() < age).sorted(nameComparator).collect(Collectors.toList())){
                output += entity + "\n";
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
