package me.sjnez.renosense.features.modules.client;

import me.sjnez.renosense.event.events.Render2DEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Logo
extends Module {
    public static final ResourceLocation mark = new ResourceLocation("textures/renosense.png");
    public Setting<Integer> imageX = this.register(new Setting<Integer>("WatermarkX", 0, 0, 300));
    public Setting<Integer> imageY = this.register(new Setting<Integer>("WatermarkY", 0, 0, 300));
    public Setting<Integer> imageWidth = this.register(new Setting<Integer>("WatermarkWidth", 97, 0, 1000));
    public Setting<Integer> imageHeight = this.register(new Setting<Integer>("WatermarkHeight", 97, 0, 1000));

    public Logo() {
        super("Logo", "Adds a RenoSense logo to your screen.", Module.Category.CLIENT, false, false, false);
    }

    public void renderLogo() {
        int width = this.imageWidth.getValue();
        int height = this.imageHeight.getValue();
        int x = this.imageX.getValue();
        int y = this.imageY.getValue();
        Logo.mc.renderEngine.bindTexture(mark);
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
        Gui.drawScaledCustomSizeModalRect((int)(x - 2), (int)(y - 36), (float)7.0f, (float)7.0f, (int)(width - 7), (int)(height - 7), (int)width, (int)height, (float)width, (float)height);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!Logo.fullNullCheck()) {
            int width = this.renderer.scaledWidth;
            int height = this.renderer.scaledHeight;
            int color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            if (((Boolean)this.enabled.getValue()).booleanValue()) {
                this.renderLogo();
            }
        }
    }
}
