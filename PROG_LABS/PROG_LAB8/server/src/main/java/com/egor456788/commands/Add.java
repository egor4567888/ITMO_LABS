package com.egor456788.commands;

import com.egor456788.Applicaton;
import com.egor456788.Printer;
import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.io.BufferedReader;


/**
 * Команда добавляющая новый элемент в коллекцию
 */
public class Add extends Command {
    final CollectionMeneger collectionMeneger;
    final Printer printer;
    final BufferedReader reader;
    final boolean silence;

    public Add(CollectionMeneger collectionMeneger, Printer printer, BufferedReader reader, boolean silence)  {
        super("add", "Добавляет элемент в коллекцию");
        this.collectionMeneger = collectionMeneger;
        this.printer = printer;
        this.reader = reader;
        this.silence = silence;
    }

    /**
     * Добавляет в коллекцию новый элемент
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
        int id = DataBaseManager.addEntity(request.getEntity());
        request.getEntity().setId(id);
        Applicaton.logger.info(id);
        collectionMeneger.add(request.getEntity());
            return (T)"Элемент добавлен";

    }
}
