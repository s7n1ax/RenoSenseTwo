package me.sjnez.renosense;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.sjnez.renosense.features.modules.misc.RPC;
import me.sjnez.renosense.util.Util;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;

public class DiscordPresence
implements Util {
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("1060009632011137034", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
        DiscordPresence.presence.largeImageText = RPC.INSTANCE.largeImageText.getValue();
        DiscordPresence.presence.smallImageKey = RPC.INSTANCE.smallImage.getValue().toString();
        DiscordPresence.presence.largeImageKey = RPC.INSTANCE.largeImage.getValue().toString();
        DiscordPresence.presence.smallImageText = RPC.INSTANCE.smallImageText.getValue();
        DiscordPresence.presence.partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
        DiscordPresence.presence.partyMax = 50;
        DiscordPresence.presence.partySize = 1;
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                GuiScreen current = DiscordPresence.mc.currentScreen;
                String s = "";
                if (current instanceof GuiMainMenu) {
                    s = "Main Menu";
                }
                if (mc.isSingleplayer()) {
                    s = "SinglePlayer";
                }
                if (DiscordPresence.mc.currentServerData != null) {
                    String string = s = RPC.INSTANCE.showIP.getValue() != false ? "On " + DiscordPresence.mc.currentServerData.serverIP + "." : " Multiplayer.";
                }
                if (current instanceof GuiInventory) {
                    s = "In Inventory";
                }
                if (current instanceof GuiChat) {
                    s = "Typing In Chat";
                }
                if (current instanceof GuiEditSign) {
                    s = "Editing a Sign";
                }
                if (current instanceof GuiChest) {
                    s = "Looking Through a Chest";
                }
                if (current instanceof GuiControls) {
                    s = "Changing Controls";
                }
                if (current instanceof GuiDownloadTerrain || current instanceof GuiConnecting) {
                    s = "Loading Into a Server";
                }
                if (current instanceof GuiOptions) {
                    s = "Looking Through Options";
                }
                if (current instanceof GuiGameOver) {
                    s = "Looking at Death Screen";
                }
                if (current instanceof GuiDisconnected) {
                    s = "Disconnected";
                }
                if (current instanceof GuiScreenServerList || current instanceof GuiScreenAddServer || current instanceof GuiMultiplayer) {
                    s = "Picking a Server";
                }
                DiscordPresence.presence.details = s;
                DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }
}
