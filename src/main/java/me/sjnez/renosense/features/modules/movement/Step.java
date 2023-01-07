package me.sjnez.renosense.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DecimalFormat;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.EntityUtil;
import me.sjnez.renosense.util.PlayerUtil;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step
extends Module {
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.VANILLA));
    public Setting<Integer> height = this.register(new Setting<Integer>("Height", 2, 1, 2));
    public Setting<Boolean> reverse = this.register(new Setting<Boolean>("Reverse", false));
    public Setting<Boolean> timer = this.register(new Setting<Boolean>("Timer", false));
    private int ticks = 0;

    public Step() {
        super("Step", "Walk up blocks quickly.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Step.mc.world == null || Step.mc.player == null) {
            return;
        }
        if (Step.mc.player.func_70090_H() || Step.mc.player.func_180799_ab() || Step.mc.player.func_70617_f_() || Step.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (this.mode.currentEnumName().equalsIgnoreCase("Normal")) {
            if (this.timer.getValue().booleanValue()) {
                if (this.ticks == 0) {
                    EntityUtil.resetTimer();
                } else {
                    --this.ticks;
                }
            }
            if (Step.mc.player != null && Step.mc.player.field_70122_E && !Step.mc.player.func_70090_H() && !Step.mc.player.func_70617_f_() && this.reverse.getValue().booleanValue()) {
                for (double y = 0.0; y < (double)this.height.getValue().intValue() + 0.5; y += 0.01) {
                    if (Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(0.0, -y, 0.0)).isEmpty()) continue;
                    Step.mc.player.field_70181_x = -10.0;
                    break;
                }
            }
            double[] dir = PlayerUtil.forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 2.6, dir[1])).isEmpty() && !Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 2.1, dir[1])).isEmpty() && !Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 1.6, dir[1])).isEmpty() && !Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 1.0, dir[1])).isEmpty() && !Step.mc.world.func_184144_a(Step.mc.player, Step.mc.player.func_174813_aQ().offset(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (Step.mc.player.field_70123_F && (Step.mc.player.field_191988_bg != 0.0f || Step.mc.player.field_70702_br != 0.0f) && Step.mc.player.field_70122_E) {
                int i;
                if (one && (double)this.height.getValue().intValue() >= 1.0) {
                    double[] oneOffset = new double[]{0.42, 0.753};
                    for (i = 0; i < oneOffset.length; ++i) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + oneOffset[i], Step.mc.player.field_70161_v, Step.mc.player.field_70122_E));
                    }
                    if (this.timer.getValue().booleanValue()) {
                        EntityUtil.setTimer(0.6f);
                    }
                    Step.mc.player.func_70107_b(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + 1.0, Step.mc.player.field_70161_v);
                    this.ticks = 1;
                }
                if (onefive && (double)this.height.getValue().intValue() >= 1.5) {
                    double[] oneFiveOffset = new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
                    for (i = 0; i < oneFiveOffset.length; ++i) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + oneFiveOffset[i], Step.mc.player.field_70161_v, Step.mc.player.field_70122_E));
                    }
                    if (this.timer.getValue().booleanValue()) {
                        EntityUtil.setTimer(0.35f);
                    }
                    Step.mc.player.func_70107_b(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + 1.5, Step.mc.player.field_70161_v);
                    this.ticks = 1;
                }
                if (two && (double)this.height.getValue().intValue() >= 2.0) {
                    double[] twoOffset = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
                    for (i = 0; i < twoOffset.length; ++i) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + twoOffset[i], Step.mc.player.field_70161_v, Step.mc.player.field_70122_E));
                    }
                    if (this.timer.getValue().booleanValue()) {
                        EntityUtil.setTimer(0.25f);
                    }
                    Step.mc.player.func_70107_b(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + 2.0, Step.mc.player.field_70161_v);
                    this.ticks = 2;
                }
                if (twofive && (double)this.height.getValue().intValue() >= 2.5) {
                    double[] twoFiveOffset = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                    for (i = 0; i < twoFiveOffset.length; ++i) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + twoFiveOffset[i], Step.mc.player.field_70161_v, Step.mc.player.field_70122_E));
                    }
                    if (this.timer.getValue().booleanValue()) {
                        EntityUtil.setTimer(0.15f);
                    }
                    Step.mc.player.func_70107_b(Step.mc.player.field_70165_t, Step.mc.player.field_70163_u + 2.5, Step.mc.player.field_70161_v);
                    this.ticks = 2;
                }
            }
        }
        if (this.mode.currentEnumName().equalsIgnoreCase("Vanilla")) {
            DecimalFormat df = new DecimalFormat("#");
            Step.mc.player.field_70138_W = Float.parseFloat(df.format(this.height.getValue()));
        }
    }

    @Override
    public void onDisable() {
        Step.mc.player.field_70138_W = 0.5f;
    }

    public String getHudInfo() {
        String t = "";
        if (this.mode.currentEnumName().equalsIgnoreCase("Normal")) {
            t = "[" + ChatFormatting.WHITE + "Normal" + ChatFormatting.GRAY + "]";
        }
        if (this.mode.currentEnumName().equalsIgnoreCase("Vanilla")) {
            t = "[" + ChatFormatting.WHITE + "Vanilla" + ChatFormatting.GRAY + "]";
        }
        return t;
    }

    public static enum Mode {
        VANILLA,
        NORMAL;

    }
}
