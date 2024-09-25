package com.egor456788.commands;

import com.egor456788.Request;
import com.egor456788.menegers.CommandManager;

/**
 * Команда выводящая историю исполненных комад
 */
public class History extends Command{
    final CommandManager commandManager;
    public History(CommandManager commandManager) {
        super("история", "выводит 15 последних команд");
        this.commandManager = commandManager;
    }

    /**
     * Выводит историю исполненных команд
     * @param args
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(Request request) {
        String args = request.getArgs();
        if (args != null)
            return (T)(getName() + ": ОШИБКА избыточное число аргументов");
        else args = "";
        String output = "";
        for (int i = commandManager.getCommandHistory().size() - 1; i > commandManager.getCommandHistory().size() - 16; i--){
            try {
                output += commandManager.getCommandHistory().get(i) + "\n";
            }
            catch (RuntimeException e){
                return (T)output;
            }
        }
        output =  output.trim().replaceAll("\\n+$", "");
        if (output == "")
            output = "История пуста";
        return (T)output;
    }
}
