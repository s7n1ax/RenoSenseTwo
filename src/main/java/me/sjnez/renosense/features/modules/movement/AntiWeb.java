package me.sjnez.renosense.features.modules.movement;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.EntityUtil;

public class AntiWeb
extends Module {
    private Setting<Boolean> HoleOnly;
    public Setting<Float> timerSpeed = this.register(new Setting<Float>("Speed", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(50.0f)));
    public float speed = 1.0f;

    public AntiWeb() {
        super("AntiWeb", "Turns on timer when in a web.", Module.Category.MOVEMENT, true, false, false);
        this.HoleOnly = this.register(new Setting<Boolean>("HoleOnly", true));
    }

    @Override
    public void onEnable() {
        this.speed = this.timerSpeed.getValue().floatValue();
    }

    @Override
    public void onUpdate() {
        if (this.HoleOnly.getValue().booleanValue()) {
            AntiWeb.mc.timer.tickLength = AntiWeb.mc.player.field_70134_J && EntityUtil.isInHole(AntiWeb.mc.player) ? 50.0f / (this.timerSpeed.getValue().floatValue() == 0.0f ? 0.1f : this.timerSpeed.getValue().floatValue()) : 50.0f;
            if (AntiWeb.mc.player.field_70122_E && EntityUtil.isInHole(AntiWeb.mc.player)) {
                AntiWeb.mc.timer.tickLength = 50.0f;
            }
        }
        if (!this.HoleOnly.getValue().booleanValue()) {
            AntiWeb.mc.timer.tickLength = AntiWeb.mc.player.field_70134_J ? 50.0f / (this.timerSpeed.getValue().floatValue() == 0.0f ? 0.1f : this.timerSpeed.getValue().floatValue()) : 50.0f;
            if (AntiWeb.mc.player.field_70122_E) {
                AntiWeb.mc.timer.tickLength = 50.0f;
            }
        }
    }
}
