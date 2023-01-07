package me.sjnez.renosense.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.HUD;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;

public class PopCounter
extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();
    public Setting<PopNotifier> popNotifier = this.register(new Setting<PopNotifier>("PopNotifier", PopNotifier.FUTURE));

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String death1(EntityPlayer player) {
        int l_Count = TotemPopContainer.get(player.getName());
        TotemPopContainer.remove(player.getName());
        if (l_Count == 1) {
            if (!this.isEnabled()) return HUD.getInstance().getCommandMessage() + ChatFormatting.GRAY + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " Totem!";
            switch (this.popNotifier.getValue()) {
                case FUTURE: {
                    return ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " totem.";
                }
                case PHOBOS: {
                    return ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + l_Count + ChatFormatting.RED + " totem.";
                }
                case DOTGOD: {
                    return ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.LIGHT_PURPLE + " time!";
                }
                case RENOSENSE: {
                    return HUD.getInstance().getCommandMessage() + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.WHITE + " Totem!";
                }
            }
            return null;
        } else {
            if (!this.isEnabled()) return HUD.getInstance().getCommandMessage() + ChatFormatting.GRAY + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " Totems!";
            switch (this.popNotifier.getValue()) {
                case FUTURE: {
                    return ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " totems.";
                }
                case PHOBOS: {
                    return ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + l_Count + ChatFormatting.RED + " totems.";
                }
                case DOTGOD: {
                    return ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.LIGHT_PURPLE + " times!";
                }
                case RENOSENSE: {
                    return HUD.getInstance().getCommandMessage() + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.WHITE + " Totems!";
                }
            }
        }
        return null;
    }

    public void onDeath(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (PopCounter.getInstance().isDisabled()) {
            return;
        }
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        if (TotemPopContainer.containsKey(player.getName())) {
            Command.sendSilentMessage(this.death1(player));
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String pop(EntityPlayer player) {
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            if (!this.isEnabled()) return HUD.getInstance().getCommandMessage() + ChatFormatting.GRAY + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " Totem.";
            switch (this.popNotifier.getValue()) {
                case FUTURE: {
                    return ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " totem.";
                }
                case PHOBOS: {
                    return ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + l_Count + ChatFormatting.RED + " totem.";
                }
                case DOTGOD: {
                    return ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " has popped " + ChatFormatting.RED + l_Count + ChatFormatting.LIGHT_PURPLE + " time in total!";
                }
                case RENOSENSE: {
                    return HUD.getInstance().getCommandMessage() + ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.WHITE + " Totem.";
                }
            }
            return "";
        } else {
            if (!this.isEnabled()) return HUD.getInstance().getCommandMessage() + ChatFormatting.GRAY + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " Totems.";
            switch (this.popNotifier.getValue()) {
                case FUTURE: {
                    return ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.GRAY + " totems.";
                }
                case PHOBOS: {
                    return ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + l_Count + ChatFormatting.RED + " totems.";
                }
                case DOTGOD: {
                    return ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " has popped " + ChatFormatting.RED + l_Count + ChatFormatting.LIGHT_PURPLE + " times in total!";
                }
                case RENOSENSE: {
                    return ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.WHITE + " Totems.";
                }
            }
        }
        return "";
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (PopCounter.getInstance().isDisabled()) {
            return;
        }
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        Command.sendSilentMessage(this.pop(player));
    }

    public static enum PopNotifier {
        PHOBOS,
        FUTURE,
        DOTGOD,
        RENOSENSE;

    }
}
