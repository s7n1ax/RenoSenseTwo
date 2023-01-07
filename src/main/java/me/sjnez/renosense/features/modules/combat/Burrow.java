package me.sjnez.renosense.features.modules.combat;

import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.BurrowUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Burrow
extends Module {
    private final Setting<Integer> offset = this.register(new Setting<Integer>("Offset", 3, -10, 10));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OBBY));
    private BlockPos originalPos;
    private int oldSlot = -1;
    Block returnBlock = null;

    public Burrow() {
        super("Burrow", "Everyone should know this module. Puts you in block.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u, Burrow.mc.player.field_70161_v);
        switch (this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.OBSIDIAN;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.ENDER_CHEST;
                break;
            }
            case EABypass: {
                this.returnBlock = Blocks.CHEST;
            }
        }
        if (Burrow.mc.world.func_180495_p(new BlockPos(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u, Burrow.mc.player.field_70161_v)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.player.field_71071_by.currentItem;
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case OBBY: {
                if (BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) break;
                Command.sendMessage("Can't find obby in hotbar!");
                this.disable();
                return;
            }
            case ECHEST: {
                if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1) break;
                Command.sendMessage("Can't find echest in hotbar!");
                this.disable();
                return;
            }
            case EABypass: {
                if (BurrowUtil.findHotbarBlock(BlockChest.class) != -1) break;
                Command.sendMessage("Can't find chest in hotbar!");
                this.disable();
                return;
            }
        }
        BurrowUtil.switchToSlot(this.mode.getValue() == Mode.OBBY ? BurrowUtil.findHotbarBlock(BlockObsidian.class) : (this.mode.getValue() == Mode.ECHEST ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockChest.class)));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u + 0.41999998688698, Burrow.mc.player.field_70161_v, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u + 0.7531999805211997, Burrow.mc.player.field_70161_v, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u + 1.00133597911214, Burrow.mc.player.field_70161_v, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u + 1.16610926093821, Burrow.mc.player.field_70161_v, true));
        BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.field_70165_t, Burrow.mc.player.field_70163_u + (double)this.offset.getValue().intValue(), Burrow.mc.player.field_70161_v, false));
        Burrow.mc.player.connection.sendPacket(new CPacketEntityAction(Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.player.func_70095_a(false);
        BurrowUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.field_72996_f) {
            if (entity.equals(Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static enum Mode {
        OBBY,
        ECHEST,
        EABypass;

    }
}
