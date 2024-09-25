package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Команда выводящая имена в порядке возрастания элементов
 */
public class PrintFieldAscendingName extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintFieldAscendingName(CollectionMeneger collectionMeneger) {
        super("print_field_ascending_name", "Выводит имена элементов в порядке возрастания");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Выводит имена в порядке возрастания элементов
     * @param request
     * @return
     * @param <T>
     */
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";


        String output = "";
        for(Entity entity: collectionMeneger.getCollection().stream().sorted().collect(Collectors.toList())){
            output += entity.getName() + "\n";
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "Коллекция пуста";
        return (T)output;
    }
}
