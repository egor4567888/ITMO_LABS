package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

public class Clear extends Command{
    final CollectionMeneger collectionMeneger;
    public Clear(CollectionMeneger collectionMeneger) {
        super("clear", "очищает коллекцию");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");
        collectionMeneger.getCollection().clear();
        return (T)"Коллекция очищена";
    }
}
