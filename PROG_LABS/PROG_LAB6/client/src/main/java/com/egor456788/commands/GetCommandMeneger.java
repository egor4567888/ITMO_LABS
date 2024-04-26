package com.egor456788.commands;

import com.egor456788.Applicaton;
import com.egor456788.menegers.CollectionMeneger;

public class GetCommandMeneger extends Command{
    public GetCommandMeneger() {
        super("getCommandMeneger", "системная команда для получения менеджера команд с сервера");

    }

    @Override
    public <T> T execute(String args) {
        return (T) Applicaton.commandManager;
    }
}
