package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

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
     * Удаляет элементы меньшие элемента с введённым индексом
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        Collections.sort(collectionMeneger.getCollection());
        try{
        collectionMeneger.getCollection().removeAll(collectionMeneger.getCollection().subList(0, Integer.parseInt(args)));
        return (T)"Элементы удалены";
        } catch (RuntimeException e){
            return (T) "Элемент не найден";
        }
    }
}
