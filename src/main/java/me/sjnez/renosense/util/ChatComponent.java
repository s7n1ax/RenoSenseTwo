package me.sjnez.renosense.util;

import java.io.File;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ChatComponent
extends TextComponentString {
    public ChatComponent(String msg) {
        super(msg);
    }

    public ChatComponent black() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.BLACK));
        return this;
    }

    public ChatComponent darkBlue() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_BLUE));
        return this;
    }

    public ChatComponent darkGreen() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_GREEN));
        return this;
    }

    public ChatComponent darkAqua() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_AQUA));
        return this;
    }

    public ChatComponent darkRed() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_RED));
        return this;
    }

    public ChatComponent darkPurple() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_PURPLE));
        return this;
    }

    public ChatComponent gold() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.GOLD));
        return this;
    }

    public ChatComponent gray() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.GRAY));
        return this;
    }

    public ChatComponent darkGray() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.DARK_GRAY));
        return this;
    }

    public ChatComponent blue() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.BLUE));
        return this;
    }

    public ChatComponent green() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.GREEN));
        return this;
    }

    public ChatComponent aqua() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.AQUA));
        return this;
    }

    public ChatComponent red() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.RED));
        return this;
    }

    public ChatComponent lightPurple() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.LIGHT_PURPLE));
        return this;
    }

    public ChatComponent yellow() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.YELLOW));
        return this;
    }

    public ChatComponent white() {
        this.func_150255_a(this.func_150256_b().setColor(TextFormatting.WHITE));
        return this;
    }

    public ChatComponent obfuscated() {
        this.func_150255_a(this.func_150256_b().setObfuscated(true));
        return this;
    }

    public ChatComponent bold() {
        this.func_150255_a(this.func_150256_b().setBold(true));
        return this;
    }

    public ChatComponent strikethrough() {
        this.func_150255_a(this.func_150256_b().setStrikethrough(true));
        return this;
    }

    public ChatComponent underline() {
        this.func_150255_a(this.func_150256_b().setUnderlined(true));
        return this;
    }

    public ChatComponent italic() {
        this.func_150255_a(this.func_150256_b().setItalic(true));
        return this;
    }

    public ChatComponent reset() {
        this.func_150255_a(this.func_150256_b().setParentStyle(null).setBold(false).setItalic(false).setObfuscated(false).setUnderlined(false).setStrikethrough(false));
        return this;
    }

    public ChatComponent setHover(ITextComponent hover) {
        this.func_150255_a(this.func_150256_b().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)));
        return this;
    }

    public ChatComponent setUrl(String url) {
        return this.setUrl(url, new KeyValueTooltipComponent("Click to visit", url));
    }

    public ChatComponent setUrl(String url, String hover) {
        return this.setUrl(url, new ChatComponent(hover).yellow());
    }

    public ChatComponent setUrl(String url, ITextComponent hover) {
        this.func_150255_a(this.func_150256_b().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, url)));
        this.setHover(hover);
        return this;
    }

    public ChatComponent setOpenFile(File filePath) {
        this.func_150255_a(this.func_150256_b().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, filePath.getAbsolutePath())));
        this.setHover(new ChatComponent(filePath.isFile() ? "Open " + filePath.getName() : "Open folder: " + filePath.toString()).yellow());
        return this;
    }

    public ChatComponent setSuggestCommand(String command) {
        this.setSuggestCommand(command, true);
        return this;
    }

    public ChatComponent setSuggestCommand(String command, boolean addTooltip) {
        this.func_150255_a(this.func_150256_b().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        if (addTooltip) {
            this.setHover(new KeyValueChatComponent("Run", command, " "));
        }
        return this;
    }

    public ChatComponent appendSibling(ITextComponent component) {
        super.func_150257_a(component);
        return this;
    }

    public ChatComponent appendFreshSibling(ITextComponent sibling) {
        this.field_150264_a.add(new TextComponentString("\n").func_150257_a(sibling));
        return this;
    }

    public static class KeyValueTooltipComponent
    extends ChatComponent {
        public KeyValueTooltipComponent(String key, String value) {
            super(key);
            this.appendText(": ");
            this.gray().appendSibling(new ChatComponent(value).yellow());
        }
    }

    public static class KeyValueChatComponent
    extends ChatComponent {
        public KeyValueChatComponent(String key, String value) {
            this(key, value, ": ");
        }

        public KeyValueChatComponent(String key, String value, String separator) {
            super(key);
            this.func_150258_a(separator);
            this.gold().appendSibling(new ChatComponent(value).yellow());
        }
    }
}
