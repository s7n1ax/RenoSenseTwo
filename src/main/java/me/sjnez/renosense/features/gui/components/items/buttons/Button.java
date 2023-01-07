package me.sjnez.renosense.features.gui.components.items.buttons;

import java.awt.Color;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.gui.RenoSenseGui;
import me.sjnez.renosense.features.gui.components.Component;
import me.sjnez.renosense.features.gui.components.items.Item;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class Button
extends Item {
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Integer tRed = ClickGui.getInstance().topRed.getValue();
        Integer tBlue = ClickGui.getInstance().topBlue.getValue();
        Integer tGreen = ClickGui.getInstance().topGreen.getValue();
        Color tColor = new Color(tRed, tGreen, tBlue, 255);
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? RenoSense.colorManager.getColorWithAlpha(ClickGui.getInstance().hoverAlpha.getValue()) : RenoSense.colorManager.getColorWithAlpha(RenoSense.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        RenoSense.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float)RenoSenseGui.getClickGui().getTextOffset(), this.getState() ? (ClickGui.getInstance().modTColor.getValue().booleanValue() ? tColor.getRGB() : -1) : -5592406);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClickR();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public void onMouseClickR() {
        this.state = !this.state;
        this.toggleR();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public void toggle() {
    }

    public void toggleR() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : RenoSenseGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }
}
