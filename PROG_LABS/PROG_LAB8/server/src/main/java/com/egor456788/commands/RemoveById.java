package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;
import java.util.Comparator;

/**
 * Команда удаляющая элемент с введённым ID
 */
public class RemoveById extends Command{
    final CollectionMeneger collectionMeneger;
    public RemoveById(CollectionMeneger collectionMeneger) {
        super("remove_by_id", "Удаляет элемент по id");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Удаляет элемент с введённым ID
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        Comparator<Entity> nameComparator = Comparator.comparing(Entity::getName);
        Collections.sort(collectionMeneger.getCollection(),nameComparator);
        try {
            collectionMeneger.getCollection().remove((Integer.parseInt(args)));
            return (T) "Элемент удалён";
        }
        catch (RuntimeException e){
            return (T) "Элемент не найден";
        }
    }
}
