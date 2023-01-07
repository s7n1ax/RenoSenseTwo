package me.sjnez.renosense.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

public class ChatHelper {
    private static final Pattern USELESS_JSON_CONTENT_PATTERN = Pattern.compile("\"[A-Za-z]+\":false,?");
    private static final int DISPLAY_DURATION = 5000;
    private final List<ITextComponent> offlineMessages = new ArrayList<ITextComponent>();
    private String[] aboveChatMessage;
    private long aboveChatMessageExpiration;

    public void sendMessage(TextFormatting color, String text) {
        this.sendMessage(new TextComponentString(text).func_150255_a(new Style().setColor(color)));
    }

    public void sendMessage(ITextComponent chatComponent) {
        if (Minecraft.getMinecraft().player == null) {
            this.putOfflineMessage(chatComponent);
        } else {
            ClientChatReceivedEvent event = new ClientChatReceivedEvent(ChatType.SYSTEM, chatComponent);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                Minecraft.getMinecraft().player.sendMessage(event.getMessage());
            }
        }
    }

    private void putOfflineMessage(ITextComponent chatComponent) {
        if (this.offlineMessages.size() == 0) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        this.offlineMessages.add(chatComponent);
    }
}
