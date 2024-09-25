package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;

/**
 * Команда выводящая информацию о коллекции
 */
public class Info extends Command{
    private final CollectionMeneger collectionMeneger;
    public Info(CollectionMeneger collectionMeneger) {
        super("info", "Выводит информацию о коллекции");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Выводит информацию о коллекции
     * @param request
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        if (collectionMeneger.getCollection().isEmpty())

            return (T)("Коллекция пуста," + " дата инициализации " + collectionMeneger.getCreationDate());
        return (T)("Тип: " + collectionMeneger.getCollection().get(0).getClass() + ", количество элементов: " + collectionMeneger.getCollection().size() + ", дата инициализации " + collectionMeneger.getCreationDate());
    }
}
