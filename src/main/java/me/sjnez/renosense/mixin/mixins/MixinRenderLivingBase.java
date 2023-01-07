package me.sjnez.renosense.mixin.mixins;

import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.modules.render.Chams;
import me.sjnez.renosense.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    @Shadow
    private static final Logger field_147923_a = LogManager.getLogger();
    @Shadow
    protected ModelBase field_77045_g;
    @Shadow
    protected boolean field_188323_j;
    float red = 0.0f;
    float green = 0.0f;
    float blue = 0.0f;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method={"doRender"}, at={@At(value="HEAD")}, cancellable=true)
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Chams.getInstance().isOn()) {
            ci.cancel();
        }
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre((EntityLivingBase)entity, (RenderLivingBase)RenderLivingBase.class.cast(this), partialTicks, x, y, z))) {
            boolean shouldSit;
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.field_77045_g.swingProgress = this.func_77040_d(entity, partialTicks);
            this.field_77045_g.isRiding = shouldSit = entity.isRiding() && entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
            this.field_77045_g.isChild = entity.isChild();
            try {
                float f = this.func_77034_a(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
                float f2 = this.func_77034_a(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                    f = this.func_77034_a(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees((float)f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                float f5 = ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
                this.func_77039_a(entity, x, y, z);
                float f6 = this.func_77044_a(entity, partialTicks);
                this.func_77043_a(entity, f6, f, partialTicks);
                float f7 = this.func_188322_c(entity, partialTicks);
                float f8 = 0.0f;
                float f9 = 0.0f;
                if (!entity.isRiding()) {
                    if (Chams.getInstance().isOn() && Chams.getInstance().lol.getValue().booleanValue()) {
                        f8 = 0.0f;
                        f9 = 0.0f;
                    } else {
                        f8 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
                        f9 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0f - partialTicks);
                    }
                    if (Chams.getInstance().isOn() && Chams.getInstance().sneak.getValue().booleanValue()) {
                        entity.setSneaking(true);
                    }
                    if (entity.isChild()) {
                        f9 *= 3.0f;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    f3 = f2 - f;
                }
                GlStateManager.enableAlpha();
                this.field_77045_g.setLivingAnimations((EntityLivingBase)entity, f9, f8, partialTicks);
                this.field_77045_g.setRotationAngles(f9, f8, f6, f3, f5, f7, (Entity)entity);
                if (this.renderOutlines) {
                    boolean flag1 = this.func_177088_c(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode((int)this.getTeamColor((Entity)entity));
                    if (!this.field_188323_j) {
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.func_177093_a(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                    if (flag1) {
                        this.func_180565_e();
                    }
                } else {
                    if (Chams.getInstance().isOn() && Chams.getInstance().players.getValue().booleanValue() && entity instanceof EntityPlayer && Chams.getInstance().mode.getValue().equals((Object)Chams.RenderMode.Solid)) {
                        this.red = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f : (float)Chams.getInstance().red.getValue().intValue() / 255.0f;
                        this.green = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f : (float)Chams.getInstance().green.getValue().intValue() / 255.0f;
                        this.blue = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f : (float)Chams.getInstance().blue.getValue().intValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glDisable(2896);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                    boolean flag1 = this.func_177090_c(entity, partialTicks);
                    if (!(entity instanceof EntityPlayer) || Chams.getInstance().isOn() && Chams.getInstance().mode.getValue().equals((Object)Chams.RenderMode.Wireframe) && Chams.getInstance().playerModel.getValue().booleanValue() || Chams.getInstance().isOff()) {
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (flag1) {
                        this.func_177091_f();
                    }
                    GlStateManager.depthMask((boolean)true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.func_177093_a(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    if (Chams.getInstance().isOn() && Chams.getInstance().players.getValue().booleanValue() && entity instanceof EntityPlayer && Chams.getInstance().mode.getValue().equals((Object)Chams.RenderMode.Wireframe)) {
                        this.red = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f : (float)Chams.getInstance().red1.getValue().intValue() / 255.0f;
                        this.green = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f : (float)Chams.getInstance().green1.getValue().intValue() / 255.0f;
                        this.blue = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f : (float)Chams.getInstance().blue1.getValue().intValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        GL11.glLineWidth(Chams.getInstance().lineWidth.getValue().floatValue());
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                    if (Chams.getInstance().isOn() && Chams.getInstance().players.getValue().booleanValue() && entity instanceof EntityPlayer && Chams.getInstance().mode.getValue().equals((Object)Chams.RenderMode.Both)) {
                        this.red = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f : (float)Chams.getInstance().red.getValue().intValue() / 255.0f;
                        this.green = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f : (float)Chams.getInstance().green.getValue().intValue() / 255.0f;
                        this.blue = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f : (float)Chams.getInstance().blue.getValue().intValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glDisable(2896);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                        this.red = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f : (float)Chams.getInstance().red1.getValue().intValue() / 255.0f;
                        this.green = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f : (float)Chams.getInstance().green1.getValue().intValue() / 255.0f;
                        this.blue = Chams.getInstance().colorSync.getValue() != false ? (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f : (float)Chams.getInstance().blue1.getValue().intValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        if (RenoSense.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        } else {
                            GL11.glColor4f(Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green, Chams.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(Chams.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue, Chams.getInstance().alpha.getValue().floatValue() / 255.0f);
                        }
                        GL11.glLineWidth(Chams.getInstance().lineWidth.getValue().floatValue());
                        this.func_77036_a(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception var20) {
                field_147923_a.error("Couldn't render entity", var20);
            }
            GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post((EntityLivingBase)entity, (RenderLivingBase)RenderLivingBase.class.cast(this), partialTicks, x, y, z));
        }
    }

    @Shadow
    protected abstract boolean func_193115_c(EntityLivingBase var1);

    @Shadow
    protected abstract float func_77040_d(T var1, float var2);

    @Shadow
    protected abstract float func_77034_a(float var1, float var2, float var3);

    @Shadow
    protected abstract float func_77044_a(T var1, float var2);

    @Shadow
    protected abstract void func_77043_a(T var1, float var2, float var3, float var4);

    @Shadow
    public abstract float func_188322_c(T var1, float var2);

    @Shadow
    protected abstract void func_180565_e();

    @Shadow
    protected abstract boolean func_177088_c(T var1);

    @Shadow
    protected abstract void func_77039_a(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract void func_177091_f();

    @Shadow
    protected abstract void func_77036_a(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract void func_177093_a(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected abstract boolean func_177090_c(T var1, float var2);
}
