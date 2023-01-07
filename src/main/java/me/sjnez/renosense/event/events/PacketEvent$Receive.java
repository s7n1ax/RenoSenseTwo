package me.sjnez.renosense.event.events;

import me.sjnez.renosense.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PacketEvent$Receive
extends PacketEvent {
    public PacketEvent$Receive(int stage, Packet<?> packet) {
        super(stage, packet);
    }
}
