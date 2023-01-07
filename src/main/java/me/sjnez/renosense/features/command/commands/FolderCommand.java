package me.sjnez.renosense.features.command.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import me.sjnez.renosense.features.command.Command;

public class FolderCommand
extends Command {
    public FolderCommand() {
        super("folder");
    }

    @Override
    public void execute(String[] commands) {
        try {
            Desktop.getDesktop().open(new File("renosense/"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
