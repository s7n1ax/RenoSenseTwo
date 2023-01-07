package me.zero.alpine.bus.type;

import me.zero.alpine.bus.EventBus;

public interface AttachableEventBus
extends EventBus {
    public void attach(EventBus var1);

    public void detach(EventBus var1);
}
