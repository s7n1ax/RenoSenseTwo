package me.sjnez.renosense.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet
extends Event {
    private Object packet;
    private Type type;

    public Packet(Object packet, Type type) {
        this.packet = packet;
        this.type = type;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        INCOMING,
        OUTGOING;

    }
}
