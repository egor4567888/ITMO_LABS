package com.egor456788.commands;

import com.egor456788.menegers.CollectionMeneger;

public class PrintNameById extends Command{
    final CollectionMeneger collectionMeneger;
    public PrintNameById(CollectionMeneger collectionMeneger) {
        super("printNameById", " выводит имя элемента по ID");
        this.collectionMeneger = collectionMeneger;
    }

    @Override
    public <T> T execute(String args) {
        try {
            Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return (T) "Ошибка ввода";
        }
        try {
            return (T) collectionMeneger.getCollection().get(Integer.parseInt(args)).getName();
        } catch (NumberFormatException e) {
            return (T) "Элемент не найден";
        }

    }
}
