package me.sjnez.renosense.features.modules.player;

import java.awt.Color;
import me.sjnez.renosense.event.events.BlockEvent;
import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.event.events.Render3DEvent;
import me.sjnez.renosense.features.Feature;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.BlockUtil;
import me.sjnez.renosense.util.MathUtil;
import me.sjnez.renosense.util.RenderUtil;
import me.sjnez.renosense.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketMine
extends Module {
    public Setting<Boolean> tweaks = this.register(new Setting<Boolean>("Tweaks", true));
    public Setting<Boolean> reset = this.register(new Setting<Boolean>("Reset", true));
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(50.0f)));
    public Setting<Boolean> silent = this.register(new Setting<Boolean>("Silent", true));
    public Setting<Boolean> noBreakAnim = this.register(new Setting<Boolean>("NoBreakAnim", false));
    public Setting<Boolean> noDelay = this.register(new Setting<Boolean>("NoDelay", false));
    public Setting<Boolean> noSwing = this.register(new Setting<Boolean>("NoSwing", false));
    public Setting<Boolean> allow = this.register(new Setting<Boolean>("AllowMultiTask", false));
    public Setting<Boolean> doubleBreak = this.register(new Setting<Boolean>("DoubleBreak", false));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", false));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(105), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue()));
    public final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.render.getValue() != false));
    public final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.render.getValue() != false));
    private static PacketMine INSTANCE = new PacketMine();
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    public float breakTime = -1.0f;
    public final Timer timer = new Timer();
    private boolean isMining = false;
    private BlockPos lastPos = null;
    private EnumFacing lastFacing = null;
    private boolean shouldSwitch = false;

    public PacketMine() {
        super("PacketMine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static PacketMine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PacketMine();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.shouldSwitch = false;
    }

    @Override
    public void onTick() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.currentPos != null && PacketMine.mc.player != null && PacketMine.mc.player.getDistanceSq(this.currentPos) > MathUtil.square(this.range.getValue().floatValue())) {
            this.currentPos = null;
            this.currentBlockState = null;
            PacketMine.mc.playerController.isHittingBlock = false;
            return;
        }
        this.onMine();
    }

    public void onMine() {
        if (!(this.currentPos == null || PacketMine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) && PacketMine.mc.world.getBlockState(this.currentPos).getBlock() != Blocks.AIR)) {
            this.currentPos = null;
            this.currentBlockState = null;
            this.shouldSwitch = true;
        }
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue().booleanValue()) {
            PacketMine.mc.playerController.blockHitDelay = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue().booleanValue()) {
            PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue().booleanValue() && PacketMine.mc.gameSettings.keyBindUseItem.isKeyDown() && !this.allow.getValue().booleanValue()) {
            PacketMine.mc.playerController.isHittingBlock = false;
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && this.currentPos != null) {
            Color color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.boxAlpha.getValue());
            RenderUtil.gradientBox(this.currentPos, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            CPacketPlayerDigging packet;
            if (this.noSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            if (this.noBreakAnim.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()) != null && packet.getPosition() != null) {
                try {
                    for (Entity entity : PacketMine.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(packet.getPosition()))) {
                        if (!(entity instanceof EntityEnderCrystal)) continue;
                        this.showAnimation();
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.getPosition(), packet.getFacing());
                }
                if (packet.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && this.reset.getValue().booleanValue() && PacketMine.mc.playerController.curBlockDamageMP > 0.1f) {
            PacketMine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue().booleanValue()) {
            BlockPos above;
            if (BlockUtil.canBreak(event.pos)) {
                if (this.currentPos == null) {
                    this.currentPos = event.pos;
                    this.currentBlockState = PacketMine.mc.world.getBlockState(this.currentPos);
                    ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE);
                    this.breakTime = pick.getDestroySpeed(this.currentBlockState) / 3.71f;
                    this.timer.reset();
                }
                PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                event.setCanceled(true);
            }
            if (this.doubleBreak.getValue().booleanValue() && BlockUtil.canBreak(above = event.pos.add(0, 1, 0)) && PacketMine.mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5.0) {
                PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                PacketMine.mc.playerController.onPlayerDestroyBlock(above);
                PacketMine.mc.world.setBlockToAir(above);
            }
        }
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        this.showAnimation(false, null, null);
    }
}
