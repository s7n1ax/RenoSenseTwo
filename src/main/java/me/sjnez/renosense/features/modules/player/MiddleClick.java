package me.sjnez.renosense.features.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.Feature;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.misc.FriendSettings;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class MiddleClick
extends Module {
    private boolean clicked = false;
    private boolean clickedbutton = false;
    private final Setting<Boolean> friend = this.register(new Setting<Boolean>("Friend", false));
    private final Setting<Boolean> pearl = this.register(new Setting<Boolean>("Pearl", false));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.MiddleClick, v -> this.pearl.getValue()));

    public MiddleClick() {
        super("MiddleClick", "Stuff for middle clicking.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (this.pearl.getValue().booleanValue() && !Feature.fullNullCheck() && this.mode.getValue() == Mode.Toggle) {
            this.throwPearl();
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        if (this.friend.getValue().booleanValue()) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked && MiddleClick.mc.currentScreen == null) {
                    this.onClick();
                }
                this.clicked = true;
            } else {
                this.clicked = false;
            }
        }
    }

    @Override
    public void onTick() {
        if (this.pearl.getValue().booleanValue() && this.mode.getValue() == Mode.MiddleClick) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clickedbutton) {
                    this.throwPearl();
                }
                this.clickedbutton = true;
            } else {
                this.clickedbutton = false;
            }
        }
    }

    private void onClick() {
        if (this.friend.getValue().booleanValue()) {
            Entity entity;
            RayTraceResult result = MiddleClick.mc.objectMouseOver;
            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
                if (RenoSense.friendManager.isFriend(entity.getName())) {
                    RenoSense.friendManager.removeFriend(entity.getName());
                    if (FriendSettings.getInstance().notify.getValue().booleanValue()) {
                        MiddleClick.mc.player.connection.sendPacket(new CPacketChatMessage("/w " + entity.getName() + " I just removed you from my friends list on RenoSense!"));
                    }
                    Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
                } else {
                    RenoSense.friendManager.addFriend(entity.getName());
                    if (FriendSettings.getInstance().notify.getValue().booleanValue()) {
                        MiddleClick.mc.player.connection.sendPacket(new CPacketChatMessage("/w " + entity.getName() + " I just added you to my friends list on RenoSense!"));
                    }
                    Command.sendMessage(ChatFormatting.GREEN + entity.getName() + ChatFormatting.GREEN + " has been friended.");
                }
            }
            this.clicked = true;
        }
    }

    private void throwPearl() {
        if (this.pearl.getValue().booleanValue()) {
            Entity entity;
            int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
            RayTraceResult result = MiddleClick.mc.objectMouseOver;
            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
                return;
            }
            boolean offhand = MiddleClick.mc.player.func_184592_cb().getItem() == Items.ENDER_PEARL;
            boolean bl = offhand;
            if (pearlSlot != -1 || offhand) {
                int oldslot = MiddleClick.mc.player.field_71071_by.currentItem;
                if (!offhand) {
                    InventoryUtil.switchToHotbarSlot(pearlSlot, false);
                }
                MiddleClick.mc.playerController.processRightClick(MiddleClick.mc.player, MiddleClick.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                if (!offhand) {
                    InventoryUtil.switchToHotbarSlot(oldslot, false);
                }
            }
        }
    }

    public static enum Mode {
        Toggle,
        MiddleClick;

    }
}
