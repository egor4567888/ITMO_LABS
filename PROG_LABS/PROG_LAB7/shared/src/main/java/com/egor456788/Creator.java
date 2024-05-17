package com.egor456788;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.exceptions.InputException;

import java.io.BufferedReader;
import java.io.IOException;

public class Creator {
    static public Entity create(Printer printer, BufferedReader reader, String userName, boolean silence) throws InputException {
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
                throw new InputException("ОШИБКА неверный формат расы");



            printer.println("Введите имя");
            line = reader.readLine();
            if (!line.trim().isEmpty() && line.matches("^[A-Za-zА-Яа-я\\s]+$"))
                name = line;
            else
                throw new InputException("ОШИБКА неверный формат имени");


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
                    throw new InputException("ОШИБКА неверный формат приверженности");
            } else
                devotion = Devotions.FLOWERS;


            printer.println("Введите возраст");
            try {
                age = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                throw new InputException("ОШИБКА неверный формат возраста");
            }
            if (age <= 0)
                throw new InputException("ОШИБКА неверный формат возраста");


            printer.println("Введите рост");
            try {
                height = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                throw new InputException("ОШИБКА неверный формат роста");
            }
            if (height <= 0)
                throw new InputException("ОШИБКА неверный формат роста");


            printer.println("Введите вес");
            try {
                weight = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                throw new InputException("ОШИБКА неверный формат веса");
            }
            if (weight <= 0)
                throw new InputException("ОШИБКА неверный формат веса");


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
                throw new InputException("ОШИБКА неверный формат пола");
        }
        catch (IOException e){
            throw new InputException("ОШИБКА чтения " + e);
        }


        if (race == Races.HEMULEN)
            return (new Hemulen(name, age, height, weight, gender, race,userName));
        else return (new Hattifattener(name, devotion, age, height, weight, gender, race,userName));

    }
}
