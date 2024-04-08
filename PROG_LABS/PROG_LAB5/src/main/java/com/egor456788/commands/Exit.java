package com.egor456788.commands;


public class Exit extends Command{

    public Exit() {
        super("exit", "выход");
    }

    @Override
    public <T> T execute(String args) {
        System.exit(0);
        return null;
    }
}
