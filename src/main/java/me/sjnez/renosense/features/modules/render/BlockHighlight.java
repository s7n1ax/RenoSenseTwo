package me.sjnez.renosense.features.modules.render;

import java.awt.Color;
import me.sjnez.renosense.event.events.Render3DEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.ColorUtil;
import me.sjnez.renosense.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class BlockHighlight
extends Module {
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> cAlpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Color Sync", false));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block you look at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, this.colorSync.getValue() != false ? new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), this.cAlpha.getValue()) : (ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.cAlpha.getValue())), this.lineWidth.getValue().floatValue(), false);
        }
    }
}
