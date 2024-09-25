package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.AccountsMeneger;

import java.util.Objects;

public class Login extends Command {
    public Login() {
        super("login", "авторизация пользователя");
    }

    @Override
    public <T> T execute(Request request) {

         if(AccountsMeneger.login(request.getUserName(),request.getPassword()) && !Objects.equals(request.getUserName(), "")){
             return (T) ("Вход успешно выполнен");
         }
         else {
             return (T) ("Неверный логин или пароль");
         }
    }
}
