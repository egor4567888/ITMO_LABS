package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Comparator;

public class PrintNameById extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintNameById(CollectionMeneger collectionMeneger) {
        super("printNameById", " выводит имя элемента по ID");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        try {
            Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return (T) "Ошибка ввода";
        }
        try {
            Comparator<Entity> nameComparator = Comparator.comparing(Entity::getName);
            return (T) collectionMeneger.getCollection().stream().sorted(nameComparator).filter(e -> e.getId() == Integer.parseInt(args)).toList().get(0).getName();
        } catch (NumberFormatException e) {
            return (T) "Элемент не найден";
        }

    }
}
