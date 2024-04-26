package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

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
     * @param args
     * @return
     * @param <T>
     */
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
