package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

public class Info extends Command{
    private final CollectionMeneger collectionMeneger;
    public Info(CollectionMeneger collectionMeneger) {
        super("info", "Выводит информацию о коллекции");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");
        return (T)("Тип: " + collectionMeneger.getCollection().get(0).getClass() + " количество элементов: " + collectionMeneger.getCollection().size() + " дата инициализации " + collectionMeneger.getCreationDate());
    }
}
