package me.sjnez.renosense.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.sjnez.renosense.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BurrowUtil
implements Util {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BurrowUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).addVector(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = BurrowUtil.mc.world.func_180495_p(neighbour).getBlock();
        if (!BurrowUtil.mc.player.isSneaking()) {
            BurrowUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BurrowUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BurrowUtil.mc.player.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            BurrowUtil.faceVector(hitVec, true);
        }
        BurrowUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BurrowUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BurrowUtil.mc.rightClickDelayTimer = 4;
        return sneaking || isSneaking;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!BurrowUtil.mc.world.func_180495_p(neighbour).getBlock().canCollideCheck(BurrowUtil.mc.world.func_180495_p(neighbour), false) || (blockState = BurrowUtil.mc.world.func_180495_p(neighbour)).func_185904_a().isReplaceable()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BurrowUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BurrowUtil.mc.player.field_70165_t, BurrowUtil.mc.player.field_70163_u + (double)BurrowUtil.mc.player.func_70047_e(), BurrowUtil.mc.player.field_70161_v);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BurrowUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BurrowUtil.mc.player.field_70177_z + MathHelper.wrapDegrees((float)(yaw - BurrowUtil.mc.player.field_70177_z)), BurrowUtil.mc.player.field_70125_A + MathHelper.wrapDegrees((float)(pitch - BurrowUtil.mc.player.field_70125_A))};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = BurrowUtil.getLegitRotations(vec);
        BurrowUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.normalizeAngle((int)((int)rotations[1]), (int)360) : rotations[1], BurrowUtil.mc.player.field_70122_E));
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.func_177958_n());
            float f2 = (float)(vec.y - (double)pos.func_177956_o());
            float f3 = (float)(vec.z - (double)pos.func_177952_p());
            BurrowUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        } else {
            BurrowUtil.mc.playerController.processRightClickBlock(BurrowUtil.mc.player, BurrowUtil.mc.world, pos, direction, vec, hand);
        }
        BurrowUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BurrowUtil.mc.rightClickDelayTimer = 4;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = BurrowUtil.mc.player.field_71071_by.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)((Object)stack.getItem())).getBlock())) continue;
            return i;
        }
        return -1;
    }

    public static void switchToSlot(int slot) {
        BurrowUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        BurrowUtil.mc.player.field_71071_by.currentItem = slot;
        BurrowUtil.mc.playerController.updateController();
    }
}
