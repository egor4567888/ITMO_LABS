package com.egor456788;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandsPack implements Serializable {
    final private ArrayList<String> commands;

    public CommandsPack(ArrayList<String> commands) {
        this.commands = commands;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }
}
