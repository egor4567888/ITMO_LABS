package com.egor456788.commands;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.menegers.*;
import com.egor456788.menegers.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;


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
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        String line;
        String name;
        Devotions devotion = null;
        int age;
        int height;
        int weight;
        Genders gender = null;
        Races race = null;
        if (silence)
            OutputBlocker.blockOutput();
        try {

            printer.println("Введите рассу (1 Хемуль, 2 Хатиффнат)");
            line = reader.readLine();
            for (Races r : Races.values()) {
                if (r.toString().equalsIgnoreCase(line)) {
                    race = r;
                    break;
                }
            }
            switch (line){
                case "1" -> race = Races.HEMULEN;
                case "2" -> race = Races.Hattifattner;
            }
            if (race == null)
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат расы");


            printer.println("Введите имя");
            line = reader.readLine();
            if (!line.trim().isEmpty() && line.matches("^[A-Za-zА-Яа-я\\s]+$"))
                name = line;
            else
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат имени");


            if (race != Races.HEMULEN) {
                printer.println("Введите приверженность (1 барометр, 2 Намира, 3 свет, 4 Боэтия)");
                line = reader.readLine();
                for (Devotions dev : Devotions.values()) {
                    if (dev.toString().equalsIgnoreCase(line)) {
                        devotion = dev;
                        break;
                    }
                }
                switch (line){
                    case "1" -> devotion = Devotions.BAROMETER;
                    case "2" -> devotion = Devotions.NAMIRA;
                    case "3" -> devotion = Devotions.LIGHT;
                    case "4" -> devotion = Devotions.BOETHIAH;
                }
                if (devotion == null)
                    return (T) (getName() + " " + args + ": ОШИБКА неверный формат приверженности");
            } else
                devotion = Devotions.FLOWERS;


            printer.println("Введите возраст");
            try {
                age = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат возраста");
            }
            if (age <= 0)
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат возраста");


            printer.println("Введите рост");
            try {
                height = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат роста");
            }
            if (height <= 0)
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат роста");


            printer.println("Введите вес");
            try {
                weight = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат веса");
            }
            if (weight <= 0)
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат веса");


            printer.println("Введите пол (1 Самец, 2 Самка, 3 Боевой вертолёт)");
            line = reader.readLine();
            for (Genders gen : Genders.values()) {
                if (gen.toString().equalsIgnoreCase(line)) {
                    gender = gen;
                    break;
                }
            }

            switch (line){
                case "1" -> gender = Genders.MALE;
                case "2" -> gender = Genders.FEMALE;
                case "3" -> gender = Genders.HELICOPTER;
            }
            if (gender == null)
                return (T) (getName() + " " + args + ": ОШИБКА неверный формат пола");
        }
        catch (IOException e){
            return (T) (getName() + " " + args + ": ОШИБКА чтения " + e);
        }


            if (race == Races.HEMULEN)
                collectionMeneger.add(new Hemulen(name, age, height, weight, gender, race));
            else collectionMeneger.add(new Hattifattener(name, devotion, age, height, weight, gender, race));
            return (T)"Элемент добавлен";

    }
}
