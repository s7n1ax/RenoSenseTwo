package me.sjnez.renosense.mixin.mixins;

import java.util.Random;
import me.sjnez.renosense.event.events.PushEvent;
import me.sjnez.renosense.features.modules.render.NoRender;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public float field_70177_z;
    @Shadow
    public float field_70125_A;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public float field_70141_P;
    @Shadow
    public World field_70170_p;
    @Shadow
    @Final
    private double[] field_191505_aI;
    @Shadow
    private long field_191506_aJ;
    @Shadow
    protected boolean field_70134_J;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    private int field_190534_ay;
    @Shadow
    private int field_70150_b;
    @Shadow
    private float field_191959_ay;
    @Shadow
    protected Random field_70146_Z;

    @Shadow
    public abstract boolean func_70051_ag();

    @Shadow
    public abstract boolean func_184218_aH();

    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    public abstract void func_174826_a(AxisAlignedBB var1);

    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();

    @Shadow
    public abstract void func_174829_m();

    @Shadow
    protected abstract void func_184231_a(double var1, boolean var3, IBlockState var4, BlockPos var5);

    @Shadow
    protected abstract boolean func_70041_e_();

    @Shadow
    public abstract boolean func_70090_H();

    @Shadow
    public abstract boolean func_184207_aI();

    @Shadow
    public abstract Entity func_184179_bs();

    @Shadow
    public abstract void func_184185_a(SoundEvent var1, float var2, float var3);

    @Shadow
    protected abstract void func_145775_I();

    @Shadow
    public abstract boolean func_70026_G();

    @Shadow
    protected abstract void func_180429_a(BlockPos var1, Block var2);

    @Shadow
    protected abstract SoundEvent func_184184_Z();

    @Shadow
    protected abstract float func_191954_d(float var1);

    @Shadow
    protected abstract boolean func_191957_ae();

    @Shadow
    public abstract void func_85029_a(CrashReportCategory var1);

    @Shadow
    protected abstract void func_70081_e(int var1);

    @Shadow
    public abstract void func_70015_d(int var1);

    @Shadow
    protected abstract int func_190531_bD();

    @Shadow
    public abstract boolean func_70027_ad();

    @Shadow
    public abstract int func_82145_z();

    @Redirect(method={"applyEntityCollision"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }

    @Inject(method={"canRenderOnFire"}, at={@At(value="HEAD")}, cancellable=true)
    public void canRenderOnFireHook(CallbackInfoReturnable<Boolean> cir) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().entityFire.getValue().booleanValue()) {
            cir.setReturnValue(false);
        }
    }
}
