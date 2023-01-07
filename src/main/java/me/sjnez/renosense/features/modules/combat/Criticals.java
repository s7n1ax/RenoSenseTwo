package me.sjnez.renosense.features.modules.combat;

import java.util.Objects;
import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals
extends Module {
    private final Setting<Integer> packets = this.register(new Setting<Integer>("Packets", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(4), "Amount of packets you want to send."));
    private final Timer timer = new Timer();
    private final boolean resetTimer = false;

    public Criticals() {
        super("Criticals", "Hits with criticals every time.", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            this.getClass();
            if (!this.timer.passedMs(0L)) {
                return;
            }
            if (Criticals.mc.player.field_70122_E && !Criticals.mc.gameSettings.keyBindJump.isKeyDown() && packet.getEntityFromWorld(Criticals.mc.world) instanceof EntityLivingBase && !Criticals.mc.player.func_70090_H() && !Criticals.mc.player.func_180799_ab()) {
                switch (this.packets.getValue()) {
                    case 1: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + (double)0.1f, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        break;
                    }
                    case 2: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 0.0625101, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 1.1E-5, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        break;
                    }
                    case 3: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 0.0625101, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 0.0125, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        break;
                    }
                    case 4: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 0.1625, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 4.0E-6, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u + 1.0E-6, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.field_70165_t, Criticals.mc.player.field_70163_u, Criticals.mc.player.field_70161_v, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                        Criticals.mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(Criticals.mc.world)));
                    }
                }
                this.timer.reset();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}
