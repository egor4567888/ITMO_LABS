package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.entities.Entity;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.util.Objects;

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
        for(Entity entity: collectionMeneger.getCollection()){
            if (Objects.equals(entity.getCreatorName(), request.getUserName())){
                DataBaseManager.deleteEntityById(entity.getId());
            }
        }
        collectionMeneger.getCollection().removeIf(entity -> Objects.equals(entity.getCreatorName(), request.getUserName()));
        return (T)"Коллекция очищена";
    }
}
