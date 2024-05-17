package com.egor456788.commands;

import com.egor456788.Applicaton;
import com.egor456788.Printer;
import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.io.BufferedReader;
import java.util.Collections;

/**
 * Команда добавляющая новый элемент в коллекцию если он меньше всех остальных в коллекции
 */
public class AddIfMin extends Command{
    final CollectionMeneger collectionMeneger;
    final Printer printer;
    final BufferedReader reader;
    final boolean silence;
    public AddIfMin(CollectionMeneger collectionMeneger, Printer printer, BufferedReader reader, boolean silence) {
        super("add_if_min", "Добавляет элемент если его возраст меньше возраста элемента с минимальным количеством очков в коллекции");
        this.collectionMeneger = collectionMeneger;
        this.printer = printer;
        this.reader = reader;
        this.silence = silence;
    }

    /**
     * Добавляет новый элемент в коллекцию если он меньше всех остальных в коллекции
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

        if (request.getEntity().getAge() >= Collections.min(collectionMeneger.getCollection()).getAge())
            return (T)"Возраст больше минимального. Элемент не будет добавлен";
        int id = DataBaseManager.addEntity(request.getEntity());
        request.getEntity().setId(id);
        Applicaton.logger.info(id);
        collectionMeneger.add(request.getEntity());

        return (T)"Элемент добавлен";

    }
    }

