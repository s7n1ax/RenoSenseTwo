package me.sjnez.renosense.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.RenderUtil;
import me.sjnez.renosense.util.Timer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

public class Trails
extends Module {
    public Setting<Integer> lifetime = this.register(new Setting<Integer>("Lifetime", 1000, 0, 5000));
    public Setting<Boolean> xp = this.register(new Setting<Boolean>("XP", false));
    public Setting<Boolean> arrow = this.register(new Setting<Boolean>("Arrow", false));
    public Setting<Boolean> self = this.register(new Setting<Boolean>("Self", false));
    public Setting<Integer> selfTime = this.register(new Setting<Integer>("Self Time", 1000, 0, 2000));
    private Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 0, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Color Sync", false));
    Setting<Boolean> target = this.register(new Setting<Boolean>("Target", false));
    Setting<Integer> targetTime = this.register(new Setting<Integer>("Target Time", 1000, 0, 2000));
    Map trails = new HashMap();

    public Trails() {
        super("Trails", "Trails for projectiles.", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Trails.mc.player != null && Trails.mc.world != null && Trails.mc.playerController != null) {
            for (Entity toRemove : Trails.mc.world.loadedEntityList) {
                if (!this.allowEntity(toRemove)) continue;
                if (this.trails.containsKey(toRemove.getUniqueID())) {
                    if (toRemove.isDead) {
                        if (((ItemTrail)this.trails.get((Object)toRemove.getUniqueID())).timer.isPaused()) {
                            ((ItemTrail)this.trails.get((Object)toRemove.getUniqueID())).timer.resetDelay();
                        }
                        ((ItemTrail)this.trails.get((Object)toRemove.getUniqueID())).timer.setPaused(false);
                        continue;
                    }
                    ((ItemTrail)this.trails.get((Object)toRemove.getUniqueID())).positions.add(new Position(toRemove.getPositionVector()));
                    continue;
                }
                this.trails.put(toRemove.getUniqueID(), new ItemTrail(toRemove));
            }
            if (this.self.getValue().booleanValue()) {
                if (this.trails.containsKey(Trails.mc.player.getUniqueID())) {
                    ItemTrail playerTrail1 = (ItemTrail)this.trails.get(Trails.mc.player.getUniqueID());
                    playerTrail1.timer.resetDelay();
                    ArrayList<Position> toRemove1 = new ArrayList<Position>();
                    for (Position position : playerTrail1.positions) {
                        if (System.currentTimeMillis() - position.time <= ((Number)this.selfTime.getValue()).longValue()) continue;
                        toRemove1.add(position);
                    }
                    playerTrail1.positions.removeAll(toRemove1);
                    playerTrail1.positions.add(new Position(Trails.mc.player.getPositionVector()));
                } else {
                    this.trails.put(Trails.mc.player.getUniqueID(), new ItemTrail(Trails.mc.player));
                }
            } else if (this.trails.containsKey(Trails.mc.player.getUniqueID())) {
                this.trails.remove(Trails.mc.player.getUniqueID());
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (Trails.mc.player != null && Trails.mc.world != null && Trails.mc.playerController != null) {
            for (Map.Entry entry : this.trails.entrySet()) {
                if (((ItemTrail)entry.getValue()).entity.isDead || Trails.mc.world.getEntityByID(((ItemTrail)entry.getValue()).entity.getEntityId()) == null) {
                    if (((ItemTrail)entry.getValue()).timer.isPaused()) {
                        ((ItemTrail)entry.getValue()).timer.resetDelay();
                    }
                    ((ItemTrail)entry.getValue()).timer.setPaused(false);
                }
                if (((ItemTrail)entry.getValue()).timer.isPassed()) continue;
                this.drawTrail((ItemTrail)entry.getValue());
            }
        }
    }

    public void drawTrail(ItemTrail trail) {
        double fadeAmount = this.normalize(System.currentTimeMillis() - trail.timer.getStartTime(), 0.0, ((Number)this.lifetime.getValue()).doubleValue());
        int alpha = (int)(fadeAmount * 255.0);
        alpha = MathHelper.clamp((int)alpha, (int)0, (int)255);
        alpha = 255 - alpha;
        alpha = trail.timer.isPaused() ? 255 : alpha;
        RenderUtil.prepare();
        GL11.glLineWidth(Float.valueOf(2.0f).floatValue());
        if (this.colorSync.getValue().booleanValue()) {
            GlStateManager.color((float)ClickGui.getInstance().red.getValue().intValue(), (float)ClickGui.getInstance().green.getValue().intValue(), (float)ClickGui.getInstance().blue.getValue().intValue(), (float)ClickGui.getInstance().alpha.getValue().intValue());
        } else {
            GlStateManager.color((float)this.red.getValue().intValue(), (float)this.green.getValue().intValue(), (float)this.blue.getValue().intValue(), (float)this.alpha.getValue().intValue());
        }
        RenderUtil.builder = RenderUtil.tessellator.getBuffer();
        RenderUtil.builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        this.buildBuffer(RenderUtil.builder, trail);
        RenderUtil.tessellator.draw();
        RenderUtil.release();
    }

    public void buildBuffer(BufferBuilder builder, ItemTrail trail) {
        for (Position p : trail.positions) {
            Vec3d pos = RenderUtil.updateToCamera(p.pos);
            double value = this.normalize(trail.positions.indexOf(p), 0.0, trail.positions.size());
            RenderUtil.addBuilderVertex(builder, pos.x, pos.y, pos.z, Color.WHITE);
        }
    }

    boolean allowEntity(Entity e) {
        return e instanceof EntityEnderPearl || e instanceof EntityExpBottle && this.xp.getValue() != false || e instanceof EntityArrow && this.arrow.getValue() != false;
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public class ItemTrail {
        public Entity entity;
        public List positions;
        public Timer timer;

        public ItemTrail(Entity entity) {
            this.entity = entity;
            this.positions = new ArrayList();
            this.timer = new Timer();
            this.timer.setDelay(((Number)Trails.this.lifetime.getValue()).longValue());
            this.timer.setPaused(true);
        }
    }

    public static class Position {
        public Vec3d pos;
        public long time;

        public Position(Vec3d pos) {
            this.pos = pos;
            this.time = System.currentTimeMillis();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && this.getClass() == o.getClass()) {
                Position position = (Position)o;
                return this.time == position.time && Objects.equals(this.pos, position.pos);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.pos, this.time);
        }
    }
}
