package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.AccountsMeneger;
import com.egor456788.menegers.DataBaseManager;

public class Register extends Command{
    public Register() {
        super("register", "регистрация пользователя");
    }

    @Override
    public <T> T execute(Request request) {

        if (AccountsMeneger.register(request.getUserName(),request.getPassword())){
            DataBaseManager.addAccount(request.getUserName(),request.getPassword());
            return (T) ( "Пользователь " + request.getUserName() + " успешно создан");
        }
        else {

            return (T) ( "Пользователь  с именем " + request.getUserName() + " уже существует");
        }

    }
}
