package me.sjnez.renosense.mixin.mixins;

import me.sjnez.renosense.features.modules.render.NoRender;
import me.sjnez.renosense.features.modules.render.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    @Final
    public Minecraft field_78455_a;
    private boolean injection = true;

    @Shadow
    public abstract void func_187457_a(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Shadow
    protected abstract void func_187456_a(float var1, float var2, EnumHandSide var3);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_1874572, float p_1874573, EnumHand hand, float p_1874575, ItemStack stack, float p_1874577, CallbackInfo info) {
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue().booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderItemSide"}, at={@At(value="HEAD")})
    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ViewModel.INSTANCE.isEnabled()) {
            GlStateManager.scale((float)((float)ViewModel.INSTANCE.scaleX.getValue().intValue() / 100.0f), (float)((float)ViewModel.INSTANCE.scaleY.getValue().intValue() / 100.0f), (float)((float)ViewModel.INSTANCE.scaleZ.getValue().intValue() / 100.0f));
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate((float)((float)ViewModel.INSTANCE.translateX.getValue().intValue() / 200.0f), (float)((float)ViewModel.INSTANCE.translateY.getValue().intValue() / 200.0f), (float)((float)ViewModel.INSTANCE.translateZ.getValue().intValue() / 200.0f));
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateX.getValue().intValue(), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateY.getValue().intValue(), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateZ.getValue().intValue(), (float)0.0f, (float)0.0f, (float)1.0f);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate((float)((float)(-ViewModel.INSTANCE.translateX.getValue().intValue()) / 200.0f), (float)((float)ViewModel.INSTANCE.translateY.getValue().intValue() / 200.0f), (float)((float)ViewModel.INSTANCE.translateZ.getValue().intValue() / 200.0f));
                GlStateManager.rotate((float)(-ViewModel.INSTANCE.rotateX.getValue().intValue()), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateY.getValue().intValue(), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.rotate((float)ViewModel.INSTANCE.rotateZ.getValue().intValue(), (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
    }
}
