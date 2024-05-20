package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;

/**
 * Команда отчищающая коллекцию
 */
public class Clear extends Command{
    final CollectionMeneger collectionMeneger;
    public Clear(CollectionMeneger collectionMeneger) {
        super("clear", "очищает коллекцию");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Отчищает коллекцию
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
        collectionMeneger.getCollection().clear();
        return (T)"Коллекция очищена";
    }
}
