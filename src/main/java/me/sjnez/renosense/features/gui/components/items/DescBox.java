package me.sjnez.renosense.features.gui.components.items;

import java.awt.Color;
import java.util.ArrayList;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.gui.components.items.Item;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.util.ColorUtil;
import me.sjnez.renosense.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class DescBox
extends Item {
    public static float descX = 690.0f;
    public static float descY = 20.0f;
    public static float descH = 50.0f;
    private final ArrayList<Item> items = new ArrayList();
    private int x2;
    private int y2;
    public boolean drag;
    public static boolean open;
    private int width;
    private int height;

    public DescBox(String name, int x, int y, boolean open) {
        super(name);
        this.x = descX;
        this.y = descY;
        this.width = 250;
        this.height = (int)descH;
        DescBox.open = open;
    }

    public void drawScreen(int mouseX, int mouseY) {
        this.drag(mouseX, mouseY);
        int tRed = 0;
        int tBlue = 0;
        int tGreen = 0;
        Color tColor = new Color(tRed, tGreen, tBlue, 119);
        int color2 = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), 255);
        if (open) {
            RenderUtil.drawRectGradient(descX, descY - 4.0f, 250.0f, descH, tColor, tColor, tColor, tColor);
        }
        Gui.drawRect((int)((int)descX), (int)((int)descY - 17), (int)((int)descX + 250), (int)((int)descY - 3), (int)(ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB() : color2));
        RenoSense.textManager.drawStringWithShadow("Description", descX + 3.0f, descY - 14.0f, -1);
    }

    public void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        descX = this.x2 + mouseX;
        descY = this.y2 + mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = (int)(descX - (float)mouseX);
            this.y2 = (int)(descY - (float)mouseY);
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            open = !open;
            Item.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return (float)mouseX >= descX && (float)mouseX <= descX + 250.0f && (float)mouseY <= descY - 3.0f && (float)mouseY >= descY - 17.0f;
    }
}
