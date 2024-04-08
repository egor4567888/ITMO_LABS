package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

public class RemoveById extends Command{
    final CollectionMeneger collectionMeneger;
    public RemoveById(CollectionMeneger collectionMeneger) {
        super("removeById", "Удаляет элемент по id");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        try {
            collectionMeneger.getCollection().remove((Integer.parseInt(args)));
            return (T) "Элемент удалён";
        }
        catch (RuntimeException e){
            return (T) "Элемент не найден";
        }
    }
}
