package me.sjnez.renosense.mixin.mixins;

import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.modules.render.Swing;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public class MixinEntityLivingBase {
    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (RenoSense.moduleManager.isModuleEnabled("Swing") && Swing.getINSTANCE().slowSwing.getValue().booleanValue()) {
            info.setReturnValue(15);
        }
    }
}
