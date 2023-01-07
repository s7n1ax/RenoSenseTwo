package me.sjnez.renosense.features.modules.render;

import me.sjnez.renosense.event.events.Packet;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing
extends Module {
    public Setting<Hand> hand = this.register(new Setting<Hand>("Mode", Hand.Offhand));
    public Setting<Boolean> slowSwing = this.register(new Setting<Boolean>("SlowSwing", false));
    private static Swing INSTANCE = new Swing();

    public Swing() {
        super("Swing", "Changes the hand you swing with.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Swing getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Swing();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        String ModeInfo = String.valueOf((Object)this.hand.getValue());
        return ModeInfo;
    }

    @Override
    public void onUpdate() {
        if (Swing.mc.world == null) {
            return;
        }
        if (this.hand.getValue().equals((Object)Hand.Offhand)) {
            Swing.mc.player.field_184622_au = EnumHand.OFF_HAND;
        } else if (this.hand.getValue().equals((Object)Hand.Mainhand)) {
            Swing.mc.player.field_184622_au = EnumHand.MAIN_HAND;
        } else if (this.hand.getValue().equals((Object)Hand.Cancel) && Swing.mc.player.func_184614_ca().getItem() instanceof ItemSword && (double)Swing.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Swing.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Swing.mc.entityRenderer.itemRenderer.itemStackMainHand = Swing.mc.player.func_184614_ca();
        }
    }

    @SubscribeEvent
    public void onPacket(Packet event) {
        if (Module.nullCheck() || event.getPacket() == Packet.Type.INCOMING) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
        }
    }

    public static enum Hand {
        Offhand,
        Mainhand,
        Cancel;

    }
}
