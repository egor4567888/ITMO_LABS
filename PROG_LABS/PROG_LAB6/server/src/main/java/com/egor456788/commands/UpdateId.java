package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.OutputBlocker;
import com.egor456788.menegers.Printer;

import java.io.BufferedReader;
import java.io.IOException;

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
        try {
            collectionMeneger.getCollection().get(Integer.parseInt(args));
        }
        catch (RuntimeException e){
            return (T)(getName() + " " + args + ": ОШИБКА Элемент не найден");
        }

        collectionMeneger.getCollection().set(Integer.parseInt(args),request.getEntity());
        return (T)"Элемент изменён";
    }
}
