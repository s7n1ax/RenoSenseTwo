package me.sjnez.renosense.features.modules.render;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chams
extends Module {
    private static Chams INSTANCE = new Chams();
    private final Setting<Page> page;
    public Setting<Boolean> players;
    public Setting<RenderMode> mode;
    public Setting<Boolean> playerModel;
    public Setting<Boolean> lol;
    public Setting<Boolean> sneak;
    public Setting<Boolean> lagChams;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> red1;
    public Setting<Integer> green1;
    public Setting<Integer> blue1;
    public final Setting<Float> alpha;
    public final Setting<Float> lineWidth;
    public Setting<Boolean> rainbow;
    public Setting<Integer> rainbowHue;
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Color Sync", false));

    public Chams() {
        super("Chams", "Draws a pretty ESP around other players.", Module.Category.RENDER, false, false, false);
        this.page = this.register(new Setting<Page>("Settings", Page.GLOBAL));
        this.players = this.register(new Setting<Object>("Render", Boolean.TRUE, v -> this.page.getValue() == Page.GLOBAL));
        this.mode = this.register(new Setting<Object>("Mode", (Object)RenderMode.Wireframe, v -> this.players.getValue() != false && this.page.getValue() == Page.GLOBAL));
        this.playerModel = this.register(new Setting<Object>("PlayerModel", Boolean.valueOf(true), v -> this.page.getValue() == Page.GLOBAL));
        this.lol = this.register(new Setting<Object>("Freeze", Boolean.valueOf(false), v -> this.page.getValue() == Page.GLOBAL));
        this.sneak = this.register(new Setting<Object>("Sneak", Boolean.valueOf(false), v -> this.page.getValue() == Page.GLOBAL));
        this.lagChams = this.register(new Setting<Object>("LagChams", Boolean.valueOf(true), v -> this.page.getValue() == Page.GLOBAL));
        this.red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.red1 = this.register(new Setting<Object>("WireframeRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.green1 = this.register(new Setting<Object>("WireframeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.blue1 = this.register(new Setting<Object>("WireframeBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.alpha = this.register(new Setting<Object>("Alpha", Float.valueOf(80.0f), Float.valueOf(0.1f), Float.valueOf(255.0f), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f), v -> this.players.getValue() != false && this.page.getValue() == Page.COLORS));
        this.rainbow = this.register(new Setting<Object>("Rainbow", Boolean.FALSE, v -> this.page.getValue() == Page.COLORS));
        this.rainbowHue = this.register(new Setting<Object>("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue() != false && this.page.getValue() == Page.COLORS));
        this.setInstance();
    }

    public static Chams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        String ModeInfo = String.valueOf((Object)this.mode.getValue());
        return ModeInfo;
    }

    @SubscribeEvent
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        event.getEntityPlayer().field_70737_aN = 0;
    }

    public static enum Page {
        COLORS,
        GLOBAL;

    }

    public static enum RenderMode {
        Solid,
        Wireframe,
        Both;

    }
}
