package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;

/**
 * Команда для завершения работы программы
 */
public class Exit extends Command{
    final private CollectionMeneger collectionMeneger;
    final private String filePath;

    public Exit(CollectionMeneger collectionMeneger, String filePath) {
        super("exit", "выход");

        this.collectionMeneger = collectionMeneger;
        this.filePath = filePath;
    }

    /**
     * Завершает работу программы
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
        collectionMeneger.save(filePath);
        System.exit(0);
        return null;
    }
}
