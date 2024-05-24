package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

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
        for(Entity e : collectionMeneger.getCollection()){
            if(e.getId() == Integer.parseInt(args)){
                if(!Objects.equals(e.getCreatorName(), request.getUserName())){
                    return (T) "Отказано в доступе";
                }
                DataBaseManager.deleteEntityById(e.getId());
                collectionMeneger.getCollection().remove(e);
                return (T) "Элемент удалён";
            }
        }
        return (T) "Элемент не найден";
    }
}
