package me.sjnez.renosense.mixin.mixins;

import java.util.Objects;
import javax.annotation.Nullable;
import me.sjnez.renosense.features.modules.client.NickHider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo func_175155_b();

    @Inject(method={"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation old = cir.getReturnValue();
        NetworkPlayerInfo info = Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(Minecraft.getMinecraft().getSession().getUsername());
        if (this.func_175155_b() != null && info != null && info.equals(this.func_175155_b())) {
            ResourceLocation mark = new ResourceLocation("minecraft", "skins/skin.png");
            if (NickHider.getInstance().isOn() && NickHider.getInstance().skinChanger.getValue().booleanValue()) {
                cir.setReturnValue(mark);
            } else if (old != null) {
                cir.setReturnValue(old);
            }
        }
    }
}
