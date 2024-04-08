package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

import java.util.Collections;

public class RemoveLower extends Command{

    final CollectionMeneger collectionMeneger;
    public RemoveLower(CollectionMeneger collectionMeneger) {
        super("removeLower", "Удаляет все элементы меньшие элемента с введёным индексом");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        Collections.sort(collectionMeneger.getCollection());
        try{
        collectionMeneger.getCollection().removeAll(collectionMeneger.getCollection().subList(0, Integer.parseInt(args)));
        return (T)"Элементы удалены";
        } catch (RuntimeException e){
            return (T) "Элемент не найден";
        }
    }
}
