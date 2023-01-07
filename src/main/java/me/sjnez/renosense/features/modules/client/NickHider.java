package me.sjnez.renosense.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;

public class NickHider
extends Module {
    public final Setting<String> NameString = this.register(new Setting<String>("Name", "New Name Here..."));
    public final Setting<Boolean> skinChanger = this.register(new Setting<Boolean>("SkinChanger", false));
    private static NickHider instance;

    public NickHider() {
        super("Media", "Change your name and skin with this module. For the skin file, you must add the directory assets/minecraft/skins/skin.png in your resource pack.", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "Success! Name succesfully changed to " + ChatFormatting.GREEN + this.NameString.getValue());
    }

    @Override
    public void onUpdate() {
        if (NickHider.mc.player == null) {
            this.disable();
        }
    }

    public static NickHider getInstance() {
        if (instance == null) {
            instance = new NickHider();
        }
        return instance;
    }
}
