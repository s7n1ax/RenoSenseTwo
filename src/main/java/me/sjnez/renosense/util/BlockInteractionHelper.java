package me.sjnez.renosense.util;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockInteractionHelper {
    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static FMLCommonHandler fmlHandler = FMLCommonHandler.instance();

    public static void placeBlockScaffold(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(BlockInteractionHelper.mc.player.field_70165_t, BlockInteractionHelper.mc.player.field_70163_u + (double)BlockInteractionHelper.mc.player.func_70047_e(), BlockInteractionHelper.mc.player.field_70161_v);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec;
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(neighbor) || !(eyesPos.squareDistanceTo(hitVec = new Vec3d(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5))) <= 18.0625)) continue;
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
            BlockInteractionHelper.processRightClickBlock(neighbor, side2, hitVec);
            BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.rightClickDelayTimer = 4;
            return;
        }
    }

    private static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BlockInteractionHelper.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BlockInteractionHelper.mc.player.field_70177_z + MathHelper.wrapDegrees((float)(yaw - BlockInteractionHelper.mc.player.field_70177_z)), BlockInteractionHelper.mc.player.field_70125_A + MathHelper.wrapDegrees((float)(pitch - BlockInteractionHelper.mc.player.field_70125_A))};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(BlockInteractionHelper.mc.player.field_70165_t, BlockInteractionHelper.mc.player.field_70163_u + (double)BlockInteractionHelper.mc.player.func_70047_e(), BlockInteractionHelper.mc.player.field_70161_v);
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = BlockInteractionHelper.getLegitRotations(vec);
        BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], BlockInteractionHelper.mc.player.field_70122_E));
    }

    private static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
        BlockInteractionHelper.getPlayerController().processRightClickBlock(BlockInteractionHelper.mc.player, BlockInteractionHelper.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockInteractionHelper.getBlock(pos).canCollideCheck(BlockInteractionHelper.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return BlockInteractionHelper.getState(pos).getBlock();
    }

    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    private static IBlockState getState(BlockPos pos) {
        return BlockInteractionHelper.mc.world.func_180495_p(pos);
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!BlockInteractionHelper.hasNeighbour(blockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = blockPos.offset(side);
                if (!BlockInteractionHelper.hasNeighbour(neighbour)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!BlockInteractionHelper.mc.world.func_180495_p(neighbour).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.func_180495_p(neighbour), false) || (blockState = BlockInteractionHelper.mc.world.func_180495_p(neighbour)).func_185904_a().isReplaceable()) continue;
            return side;
        }
        return null;
    }

    public static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.offset(side);
            if (BlockInteractionHelper.mc.world.func_180495_p(neighbour).func_185904_a().isReplaceable()) continue;
            return true;
        }
        return false;
    }
}
