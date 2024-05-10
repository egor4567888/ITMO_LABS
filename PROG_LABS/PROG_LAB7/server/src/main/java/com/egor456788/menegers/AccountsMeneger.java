package com.egor456788.menegers;

import com.egor456788.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public  class AccountsMeneger {
    private static final Map<String, String> accounts = new HashMap<>();
    public static String register(String name, String password){
        if (accounts.get(name)== null){
            return "Пользователь " + name + " создан";
        }
        else {
            accounts.put(name,password);
            return "Пользователь  с именем " + name + " уже существует";
        }
    }
    public static boolean login(String name, String password){
        if (Objects.equals(accounts.get(name), password))
            return true;
        else {
            return false;
        }
    }
}
