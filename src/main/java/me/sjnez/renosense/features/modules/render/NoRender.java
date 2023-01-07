package me.sjnez.renosense.features.modules.render;

import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {
    private static NoRender INSTANCE = new NoRender();
    public Setting<Boolean> entityFire = this.register(new Setting<Boolean>("Entity Fire", Boolean.valueOf(false), "Don't render fire on entities"));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", Boolean.valueOf(false), "Blocks"));
    public Setting<NoArmor> noArmor = this.register(new Setting<NoArmor>("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
    public Setting<Skylight> skylight = this.register(new Setting<Skylight>("Skylight", Skylight.NONE));
    public Setting<Boolean> advancements = this.register(new Setting<Boolean>("Advancements", false));
    public Setting<Boolean> hurtCam = this.register(new Setting<Boolean>("NoHurtCam", false));
    public Setting<Boolean> fire = this.register(new Setting<Boolean>("Fire", Boolean.valueOf(false), "Removes the portal overlay."));
    public Setting<Boolean> explosion = this.register(new Setting<Boolean>("Explosions", Boolean.valueOf(false), "Removes explosions"));
    public Setting<Boolean> noFog = this.register(new Setting<Boolean>("NoFog", Boolean.valueOf(false), "Removes Fog"));

    public NoRender() {
        super("NoRender", "Allows you to stop rendering stuff.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion && this.explosion.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void fog_density(EntityViewRenderEvent.FogDensity event) {
        if (this.noFog.getValue().booleanValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    static {
        INSTANCE = new NoRender();
    }

    public static enum NoArmor {
        NONE,
        ALL,
        HELMET;

    }

    public static enum Skylight {
        NONE,
        WORLD,
        ENTITY,
        ALL;

    }
}
