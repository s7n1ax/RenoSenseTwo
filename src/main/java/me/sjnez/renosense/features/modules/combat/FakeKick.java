package me.sjnez.renosense.features.modules.combat;

import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class FakeKick
extends Module {
    private final Setting<Boolean> healthDisplay = this.register(new Setting<Boolean>("HealthDisplay", false));

    public FakeKick() {
        super("FakeKick", "Log with the press of a button.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (!mc.isSingleplayer()) {
            if (this.healthDisplay.getValue().booleanValue()) {
                float health = FakeKick.mc.player.func_110139_bj() + FakeKick.mc.player.func_110143_aJ();
                Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged out with " + health + " health remaining.")));
                this.disable();
            }
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Internal Exception: java.lang.NullPointerException")));
            this.disable();
        }
        Command.sendMessage("Cannot fake kick in single player lil bro.");
        this.disable();
    }
}
