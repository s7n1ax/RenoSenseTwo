package me.sjnez.renosense.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.command.Command;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : RenoSense.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + RenoSense.commandManager.getPrefix() + command.getName());
        }
    }
}
