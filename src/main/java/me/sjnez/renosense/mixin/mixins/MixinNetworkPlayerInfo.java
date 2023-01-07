package me.sjnez.renosense.mixin.mixins;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import me.sjnez.renosense.features.modules.client.NickHider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={NetworkPlayerInfo.class})
public abstract class MixinNetworkPlayerInfo {
    @Shadow
    public abstract GameProfile func_178845_a();

    @Inject(method={"getLocationSkin"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation old = cir.getReturnValue();
        NetworkPlayerInfo info = Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(Minecraft.getMinecraft().getSession().getUsername());
        if (info != null && this.func_178845_a().equals(info.getGameProfile())) {
            ResourceLocation mark = new ResourceLocation("minecraft", "skins/skin.png");
            if (NickHider.getInstance().isOn() && NickHider.getInstance().skinChanger.getValue().booleanValue()) {
                cir.setReturnValue(mark);
            } else if (old != null) {
                cir.setReturnValue(old);
            }
        }
    }
}
