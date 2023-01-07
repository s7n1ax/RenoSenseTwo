package me.sjnez.renosense.features.modules.render;

import me.sjnez.renosense.event.events.PerspectiveEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aspect
extends Module {
    public Setting<Double> aspect;

    public Aspect() {
        super("AspectRatio", "Stretched res like fortnite.", Module.Category.RENDER, true, false, false);
        this.aspect = this.register(new Setting<Double>("aspect", (double)(Aspect.mc.displayWidth / Aspect.mc.displayHeight) + 0.0, 0.0, 3.0));
    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue().floatValue());
    }
}
