package me.sjnez.renosense.mixin.mixins;

import java.awt.Color;
import java.util.List;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.modules.client.HUD;
import me.sjnez.renosense.features.modules.misc.ChatModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat
extends Gui {
    @Shadow
    @Final
    public List<ChatLine> field_146253_i;
    private ChatLine chatLine;

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(int left, int top, int right, int bottom, int color) {
        Gui.drawRect((int)left, (int)top, (int)right, (int)bottom, (int)(ChatModifier.getInstance().isOn() && ChatModifier.getInstance().clean.getValue() != false ? 0 : color));
    }

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
        if (text.contains("§+")) {
            float colorSpeed = 101 - HUD.getInstance().rainbowSpeed.getValue();
            RenoSense.textManager.drawRainbowString(text, x, y, Color.HSBtoRGB(HUD.getInstance().hue, 1.0f, 1.0f), 100.0f, true);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return 0;
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=0, remap=false))
    public int drawnChatLinesSize(List<ChatLine> list) {
        return ChatModifier.getInstance().isOn() && ChatModifier.getInstance().infinite.getValue() != false ? -2147483647 : list.size();
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=2, remap=false))
    public int chatLinesSize(List<ChatLine> list) {
        return ChatModifier.getInstance().isOn() && ChatModifier.getInstance().infinite.getValue() != false ? -2147483647 : list.size();
    }
}
