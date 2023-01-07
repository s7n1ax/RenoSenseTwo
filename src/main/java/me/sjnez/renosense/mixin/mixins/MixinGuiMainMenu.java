package me.sjnez.renosense.mixin.mixins;

import java.awt.Desktop;
import java.net.URI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu
extends GuiScreen {
    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 1000) {
            try {
                Desktop.getDesktop().browse(URI.create("https://discord.gg/kncfFHmfc4"));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}
