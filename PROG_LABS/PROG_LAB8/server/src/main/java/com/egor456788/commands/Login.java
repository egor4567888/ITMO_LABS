package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.AccountsMeneger;

public class Login extends Command {
    public Login() {
        super("login", "авторизация пользователя");
    }

    @Override
    public <T> T execute(Request request) {
         if(AccountsMeneger.login(request.getUserName(),request.getPassword())){
             return (T) ("Вход успешно выполнен");
         }
         else {
             return (T) ("Неверный логин или пароль");
         }
    }
}
