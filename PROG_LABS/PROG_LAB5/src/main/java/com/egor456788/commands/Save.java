package com.egor456788.commands;

import com.egor456788.entities.Entity;
import com.egor456788.entities.Hemulen;
import com.egor456788.menegers.CollectionMeneger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Команда сохраняющая коллекцию в введённый файл в формате xml
 */
public class Save extends Command{
    final CollectionMeneger collectionMeneger;
    public Save(CollectionMeneger collectionMeneger) {
        super("save", "Сохраняет коллекцию");
        this.collectionMeneger = collectionMeneger;
    }

    /**
     * Команда сохраняющая коллекцию в введённый файл в формате xml
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {

        XStream xstream = new XStream(new DomDriver());

        xstream.alias("Hemulen", Hemulen.class);
        xstream.alias("Hattifattener", com.egor456788.entities.Hattifattener.class);
        xstream.alias("devotion", com.egor456788.common.Devotions.class);
        xstream.alias("gender", com.egor456788.common.Genders.class);
        xstream.alias("race", com.egor456788.common.Races.class);
        xstream.alias("condition", com.egor456788.common.Conditions.class);
        xstream.alias("phrase",com.egor456788.common.Phrases.class);
        xstream.omitField(Entity.class,"score");

        try (FileWriter writer = new FileWriter(args)) {
            xstream.toXML(collectionMeneger.getCollection(), writer);
            return (T) ("Список объектов сохранен в " + args);
        } catch (Exception e) {
            return (T)(getName() + " " + args + ": ОШИБКА файл не найден");
        }
    }
}
