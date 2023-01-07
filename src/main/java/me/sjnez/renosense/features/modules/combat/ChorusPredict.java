package me.sjnez.renosense.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.event.events.Render3DEvent;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.ColorUtil;
import me.sjnez.renosense.util.RenderUtil;
import me.sjnez.renosense.util.Timer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChorusPredict
extends Module {
    private final Timer renderTimer = new Timer();
    private BlockPos pos;
    private final Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", true));
    private final Setting<Integer> renderDelay = this.register(new Setting<Integer>("RenderDelay", 4000, 0, 4000));
    private Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    private Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> outlineAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Color Sync", false));

    public ChorusPredict() {
        super("ChorusPredict", "Predicts the chorus, and renders where they will teleport to.", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && ((packet = (SPacketSoundEffect)event.getPacket()).getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT)) {
            this.renderTimer.reset2();
            this.pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("A player chorused to: " + ChatFormatting.AQUA + "X: " + this.pos.getX() + ", Y: " + this.pos.getY() + ", Z: " + this.pos.getZ());
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.pos != null) {
            if (this.renderTimer.passed(this.renderDelay.getValue().intValue())) {
                this.pos = null;
                return;
            }
            RenderUtil.drawBoxESP(this.pos, this.colorSync.getValue() != false ? new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().alpha.getValue()) : (this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue())), 1.5f, true, true, this.alpha.getValue());
        }
    }
}
