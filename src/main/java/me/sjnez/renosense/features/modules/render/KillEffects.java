package me.sjnez.renosense.features.modules.render;

import java.util.ArrayList;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class KillEffects
extends Module {
    public Setting<Boolean> thunder = this.register(new Setting<Boolean>("Thunder", true));
    public Setting<Integer> numbersThunder = this.register(new Setting<Integer>("Number Thunder", 1, 1, 10));
    public Setting<Boolean> sound = this.register(new Setting<Boolean>("Sound", true));
    public Setting<Integer> numberSound = this.register(new Setting<Integer>("Number Sound", 1, 1, 10));
    ArrayList<EntityPlayer> playersDead = new ArrayList();

    public KillEffects() {
        super("KillEffects", "Effects when you kill.", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.playersDead.clear();
    }

    @Override
    public void onUpdate() {
        if (KillEffects.mc.world == null) {
            this.playersDead.clear();
            return;
        }
        KillEffects.mc.world.playerEntities.forEach(entity -> {
            if (this.playersDead.contains(entity)) {
                if (entity.getHealth() > 0.0f) {
                    this.playersDead.remove(entity);
                }
            } else if (entity.getHealth() == 0.0f) {
                int i;
                if (this.thunder.getValue().booleanValue()) {
                    for (i = 0; i < this.numbersThunder.getValue(); ++i) {
                        KillEffects.mc.world.spawnEntity(new EntityLightningBolt(KillEffects.mc.world, entity.posX, entity.posY, entity.posZ, true));
                    }
                }
                if (this.sound.getValue().booleanValue()) {
                    for (i = 0; i < this.numberSound.getValue(); ++i) {
                        KillEffects.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.0f);
                    }
                }
                this.playersDead.add((EntityPlayer)entity);
            }
        });
    }
}
