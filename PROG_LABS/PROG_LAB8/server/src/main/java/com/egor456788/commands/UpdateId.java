package com.egor456788.commands;

import com.egor456788.Printer;
import com.egor456788.Request;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.DataBaseManager;

import java.io.BufferedReader;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Команда обновляющая элемент с введённым ID
 */
public class UpdateId extends Command{
    final CollectionMeneger collectionMeneger;
    final Printer printer;
    final BufferedReader reader;
    final boolean silence;

    /**
     * Обновляет элемент с введённым ID
     * @param collectionMeneger
     * @param printer
     * @param reader
     * @param silence
     */
    public UpdateId(CollectionMeneger collectionMeneger, Printer printer, BufferedReader reader, boolean silence) {
        super("update_id", "Обновляет элемент по id");
        this.collectionMeneger = collectionMeneger;
        this.printer = printer;
        this.reader = reader;
        this.silence = silence;
    }

    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();

        OptionalInt index = IntStream.range(0, collectionMeneger.getCollection().size())
                .filter(i -> collectionMeneger.getCollection().get(i).getId() == Integer.parseInt(request.getArgs()))
                .findFirst();
        if(!index.isPresent()) {
            return (T)(getName() + " " + args + ": ОШИБКА Элемент не найден");
        }
        request.getEntity().setId(Integer.parseInt(request.getArgs()));
        collectionMeneger.getCollection().set(index.getAsInt(),request.getEntity());
        DataBaseManager.updateEntity(request.getEntity());
        return (T)"Элемент изменён";
    }
}
