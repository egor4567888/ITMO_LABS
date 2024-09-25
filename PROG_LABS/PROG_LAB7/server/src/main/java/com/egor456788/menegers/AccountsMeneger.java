package com.egor456788.menegers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public  class AccountsMeneger {
    private static final  Map<String, String> accounts = new HashMap<>();
    public static boolean register(String name, String password){
        if (accounts.get(name)== null){
            accounts.put(name,password);
            return true;
        }
        else {

            return false;
        }
    }
    public static void clear(){
        accounts.clear();
    }
    public static boolean login(String name, String password){
        if (Objects.equals(accounts.get(name), password))
            return true;
        else {
            return false;
        }
    }
    public static Map getAcc(){
        return accounts;
    }
}
