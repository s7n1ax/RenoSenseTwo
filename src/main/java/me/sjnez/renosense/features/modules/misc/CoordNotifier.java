package me.sjnez.renosense.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Collection;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.ChatComponent;
import me.sjnez.renosense.util.ChatHelper;
import net.minecraft.client.network.NetworkPlayerInfo;

public class CoordNotifier
extends Module {
    private Setting<Boolean> safety = this.register(new Setting<Boolean>("Safety", false));

    public CoordNotifier() {
        super("CoordNotifier", "Send your coordinates to friends.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (mc.isSingleplayer()) {
            this.disable();
        }
        Collection InfoMap = CoordNotifier.mc.player.connection.getPlayerInfoMap();
        Command.sendMessage(ChatFormatting.RED + "Send your coordinates to which friends?");
        for (NetworkPlayerInfo p : InfoMap) {
            String getUrl = "/w " + p.getGameProfile().getName() + " " + this.coordinates() + " Here are my coordinates!";
            ChatHelper ch = new ChatHelper();
            if (!RenoSense.friendManager.isFriend(p.getGameProfile().getName()) || this.safety.getValue().booleanValue() && !this.isSafe()) continue;
            ch.sendMessage(new ChatComponent(p.getGameProfile().getName()).green().bold().setUrl(getUrl, "Click on this to send them your coordinates!").italic());
        }
        this.disable();
    }

    public boolean isSafe() {
        return this.safety.getValue() != false && CoordNotifier.mc.player.getPosition().getX() < 5000 && CoordNotifier.mc.player.getPosition().getY() < 5000 && CoordNotifier.mc.player.getPosition().getX() > -5000 && CoordNotifier.mc.player.getPosition().getY() > -5000;
    }

    public String coordinates() {
        boolean inHell = CoordNotifier.mc.world.getBiome(CoordNotifier.mc.player.getPosition()).getBiomeName().equals("Hell");
        int posX = (int)CoordNotifier.mc.player.posX;
        int posY = (int)CoordNotifier.mc.player.posY;
        int posZ = (int)CoordNotifier.mc.player.posZ;
        float nether = !inHell ? 0.125f : 8.0f;
        int hposX = (int)(CoordNotifier.mc.player.posX * (double)nether);
        int hposZ = (int)(CoordNotifier.mc.player.posZ * (double)nether);
        return inHell ? posX + " [" + hposX + "], " + posY + ", " + posZ + " [" + hposZ + "]" : posX + " [" + hposX + "], " + posY + ", " + posZ + " [" + hposZ + "]";
    }
}
