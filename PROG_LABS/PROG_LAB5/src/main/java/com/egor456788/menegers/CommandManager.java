package com.egor456788.menegers;

import com.egor456788.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер ккоманд
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();


    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }


    public Map<String, Command> getCommands() {
        return commands;
    }


    public List<String> getCommandHistory() {
        return commandHistory;
    }


    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}