package com.egor456788.commands;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.Printer;

import java.util.Collections;
import java.util.Scanner;

public class AddIfMin extends Command{
    final CollectionMeneger collectionMeneger;
    final Printer printer;
    public AddIfMin(CollectionMeneger collectionMeneger, Printer printer) {
        super("addIfMin", "Добавляет элемент если его возраст меньше возраста элемента с минимальным количеством очков в коллекции");
        this.collectionMeneger = collectionMeneger;
        this.printer = printer;
    }

    @Override
    public <T> T execute(String args) {
        if (args != null)
            return (T)(getName() + " " + args + ": ОШИБКА избыточное число аргументов");
        String line;
        String name;
        Devotions devotion = null;
        int age;
        int height;
        int weight;
        Genders gender = null;
        Races race = null;
        boolean isValid = false;
        Scanner scanner = new Scanner(System.in);

        printer.println("Введите рассу (Хемуль, Хатиффнат)");
        line = scanner.nextLine();
        for (Races r : Races.values()) {
            if (r.toString().equalsIgnoreCase(line)) {
                isValid = true;
                race = r;
                break;
            }
        }
        if (!isValid)
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат расы");
        isValid = false;


        printer.println("Введите имя");
        line = scanner.nextLine();
        if (!line.trim().isEmpty() && line.matches("^[A-Za-zА-Яа-я\\s]+$"))
            name = line;
        else
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат имени");


        if (race != Races.HEMULEN) {
            printer.println("Введите приверженность (барометр, Намира, свет, Боэтия)");
            line = scanner.nextLine();
            for (Devotions dev : Devotions.values()) {
                if (dev.toString().equalsIgnoreCase(line)) {
                    isValid = true;
                    devotion = dev;
                    break;
                }
            }
            if (!isValid)
                return (T)(getName() + " " + args + ": ОШИБКА неверный формат приверженности");
        }
        else
            devotion = Devotions.FLOWERS;
        isValid = false;


        printer.println("Введите возраст");
        try{
            age = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e){
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат возраста");
        }
        if (age <= 0)
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат возраста");

        printer.println("Введите рост");
        try{
            height = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e){
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат роста");
        }
        if (height <= 0)
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат роста");


        printer.println("Введите вес");
        try{
            weight = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e){
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат веса");
        }
        if (weight <= 0)
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат веса");


        printer.println("Введите пол (Самец, Самка, Боевой вертолёт)");
        line = scanner.nextLine();
        for (Genders gen : Genders.values()) {
            if (gen.toString().equalsIgnoreCase(line)) {
                isValid = true;
                gender = gen;
                break;
            }
        }
        if (!isValid)
            return (T)(getName() + " " + args + ": ОШИБКА неверный формат пола");
        isValid = false;


        if (age >= Collections.min(collectionMeneger.getCollection()).getAge())
            return (T)"Возраст больше минимального. Элемент не будет добавлен";
        if (race == Races.HEMULEN)
            collectionMeneger.add(new Hemulen(name, age, height, weight, gender, race));
        else collectionMeneger.add(new Hattifattener(name, devotion, age, height, weight, gender, race));
        return (T)"Элемент добавлен";

    }
    }

