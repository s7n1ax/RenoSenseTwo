package me.sjnez.renosense.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.sjnez.renosense.event.events.Render3DEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.ColorUtil;
import me.sjnez.renosense.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BurrowESP
extends Module {
    private static BurrowESP INSTANCE = new BurrowESP();
    public Setting<Integer> range = this.register(new Setting<Integer>("Range", 20, 5, 50));
    public Setting<Boolean> self = this.register(new Setting<Boolean>("Self", true));
    public Setting<Boolean> text = this.register(new Setting<Boolean>("Text", true));
    public Setting<String> textString = this.register(new Setting<Object>("TextString", "BURROW", v -> this.text.getValue()));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 0, 0, 255));
    public Setting<Integer> outlineAlpha = this.register(new Setting<Integer>("OL-Alpha", 0, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Color Sync", false));
    private final List<BlockPos> posList = new ArrayList<BlockPos>();
    private RenderUtil renderUtil = new RenderUtil();

    public BurrowESP() {
        super("BurrowESP", "See who is in a burrow.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static BurrowESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BurrowESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        this.posList.clear();
        for (EntityPlayer player : BurrowESP.mc.world.field_73010_i) {
            BlockPos blockPos = new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u + 0.2), Math.floor(player.field_70161_v));
            if (BurrowESP.mc.world.func_180495_p(blockPos).getBlock() != Blocks.ENDER_CHEST && BurrowESP.mc.world.func_180495_p(blockPos).getBlock() != Blocks.OBSIDIAN || !(blockPos.func_177954_c(BurrowESP.mc.player.field_70165_t, BurrowESP.mc.player.field_70163_u, BurrowESP.mc.player.field_70161_v) <= (double)this.range.getValue().intValue()) || blockPos.func_177954_c(BurrowESP.mc.player.field_70165_t, BurrowESP.mc.player.field_70163_u, BurrowESP.mc.player.field_70161_v) <= 1.5 && !this.self.getValue().booleanValue()) continue;
            this.posList.add(blockPos);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (BlockPos blockPos : this.posList) {
            String s = this.textString.getValue().toUpperCase();
            if (this.text.getValue().booleanValue()) {
                this.renderUtil.drawText(blockPos, s, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue()));
            }
            RenderUtil.drawBoxESP(blockPos, this.colorSync.getValue() != false ? new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().alpha.getValue()) : (this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue())), 1.5f, true, true, this.alpha.getValue());
        }
    }
}
