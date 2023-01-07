package me.sjnez.renosense.features.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.BlockUtil;
import me.sjnez.renosense.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleFiller
extends Module {
    private Setting<Double> range = this.register(new Setting<Double>("Range", 4.5, 0.1, 6.0));
    private Setting<Boolean> smart = this.register(new Setting<Boolean>("Smart", false));
    private Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private Setting<Boolean> packetPlace = this.register(new Setting<Boolean>("PacketPlace", false));
    private Setting<Double> smartRange = this.register(new Setting<Double>("Smart Range", 4.0, 0.1, 6.0));
    private BlockPos render;
    private boolean isSneaking;
    private EntityPlayer closestTarget;
    private boolean caOn;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        Object packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }

    @Override
    public void onEnable() {
        if (RenoSense.moduleManager.isModuleEnabled("AutoCrystal") || RenoSense.moduleManager.isModuleEnabled("AutoCrystal")) {
            this.caOn = true;
        }
        super.onEnable();
    }

    @Override
    public void onUpdate() {
        if (HoleFiller.mc.world == null) {
            return;
        }
        if (this.smart.getValue().booleanValue()) {
            this.findClosestTarget();
        }
        List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        double dist = 0.0;
        double prevDist = 0.0;
        int n = HoleFiller.mc.player.func_184614_ca().getItem() == Item.getItemFromBlock((Block)Blocks.OBSIDIAN) ? HoleFiller.mc.player.field_71071_by.currentItem : -1;
        int obsidianSlot = n;
        if (obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (HoleFiller.mc.player.field_71071_by.getStackInSlot(l).getItem() != Item.getItemFromBlock((Block)Blocks.OBSIDIAN)) continue;
                obsidianSlot = l;
                break;
            }
        }
        if (obsidianSlot == -1) {
            return;
        }
        for (BlockPos blockPos : blocks) {
            if (!HoleFiller.mc.world.func_72872_a(Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) continue;
            if (this.smart.getValue().booleanValue() && this.isInRange(blockPos)) {
                q = blockPos;
                continue;
            }
            q = blockPos;
        }
        this.render = q;
        if (q != null && HoleFiller.mc.player.field_70122_E) {
            int oldSlot = HoleFiller.mc.player.field_71071_by.currentItem;
            if (HoleFiller.mc.player.field_71071_by.currentItem != obsidianSlot) {
                HoleFiller.mc.player.field_71071_by.currentItem = obsidianSlot;
            }
            BlockUtil.placeBlock(this.render, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packetPlace.getValue(), this.isSneaking);
            HoleFiller.mc.player.swingArm(EnumHand.MAIN_HAND);
            HoleFiller.mc.player.field_71071_by.currentItem = oldSlot;
            HoleFiller.resetRotation();
        }
    }

    private double getDistanceToBlockPos(BlockPos pos1, BlockPos pos2) {
        double x = pos1.func_177958_n() - pos2.func_177958_n();
        double y = pos1.func_177956_o() - pos2.func_177956_o();
        double z = pos1.func_177952_p() - pos2.func_177952_p();
        return Math.sqrt(x * x + y * y + z * z);
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        HoleFiller.setYawAndPitch((float)v[0], (float)v[1]);
    }

    public boolean IsHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return !(HoleFiller.mc.world.func_180495_p(boost).getBlock() != Blocks.AIR || HoleFiller.mc.world.func_180495_p(boost2).getBlock() != Blocks.AIR || HoleFiller.mc.world.func_180495_p(boost7).getBlock() != Blocks.AIR || HoleFiller.mc.world.func_180495_p(boost3).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.func_180495_p(boost3).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.func_180495_p(boost4).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.func_180495_p(boost4).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.func_180495_p(boost5).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.func_180495_p(boost5).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.func_180495_p(boost6).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.func_180495_p(boost6).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.func_180495_p(boost8).getBlock() != Blocks.AIR || HoleFiller.mc.world.func_180495_p(boost9).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.func_180495_p(boost9).getBlock() != Blocks.BEDROCK);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleFiller.mc.player.field_70165_t), Math.floor(HoleFiller.mc.player.field_70163_u), Math.floor(HoleFiller.mc.player.field_70161_v));
    }

    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.field_70165_t), Math.floor(this.closestTarget.field_70163_u), Math.floor(this.closestTarget.field_70161_v));
        }
        return null;
    }

    private void findClosestTarget() {
        List playerList = HoleFiller.mc.world.field_73010_i;
        this.closestTarget = null;
        for (EntityPlayer target : playerList) {
            if (target == HoleFiller.mc.player || RenoSense.friendManager.isFriend(target.getName()) || !EntityUtil.isLiving(target) || target.func_110143_aJ() <= 0.0f) continue;
            if (this.closestTarget == null) {
                this.closestTarget = target;
                continue;
            }
            if (HoleFiller.mc.player.func_70032_d(target) >= HoleFiller.mc.player.func_70032_d(this.closestTarget)) continue;
            this.closestTarget = target;
        }
    }

    private boolean isInRange(BlockPos blockPos) {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(HoleFiller.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        return positions.contains(blockPos);
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.create();
        if (this.smart.getValue().booleanValue() && this.closestTarget != null) {
            positions.addAll((Collection)this.getSphere(this.getClosestTargetPos(), this.smartRange.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        } else if (!this.smart.getValue().booleanValue()) {
            positions.addAll((Collection)this.getSphere(HoleFiller.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        }
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.func_177958_n();
        int cy = loc.func_177956_o();
        int cz = loc.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (f >= f2) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = HoleFiller.mc.player.field_70177_z;
            pitch = HoleFiller.mc.player.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        HoleFiller.resetRotation();
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        super.onDisable();
    }
}
