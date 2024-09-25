package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.util.Collections;
import java.util.Objects;

/**
 * Команда удаляющая элементы меньшие элемента с введённым индексом
 */
public class RemoveLower extends Command{

    final CollectionMeneger collectionMeneger;
    public RemoveLower(CollectionMeneger collectionMeneger) {
        super("remove_lower", "Удаляет все элементы меньшие элемента с введённым индексом");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Удаляет элементы меньшие элемента с введённым индексом (сравнение по возрасту)
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        int min = Integer.parseInt(request.getArgs());

        for(Entity entity: collectionMeneger.getCollection()){
            if(entity.getAge() < min && Objects.equals(entity.getCreatorName(), request.getUserName())){
                DataBaseManager.deleteEntityById(entity.getId());
            }
        }
        collectionMeneger.getCollection().removeIf(entity -> entity.getAge() < min && Objects.equals(entity.getCreatorName(), request.getUserName()));

        return (T)"Элементы удалены";
    }
}
