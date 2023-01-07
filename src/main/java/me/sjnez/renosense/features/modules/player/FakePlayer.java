package me.sjnez.renosense.features.modules.player;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.sjnez.renosense.features.command.Command;
import me.sjnez.renosense.features.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer
extends Module {
    private final String name = "Scott";
    private EntityOtherPlayerMP _fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing.", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onEnable() {
        if (FakePlayer.fullNullCheck()) {
            this.disable();
            return;
        }
        this._fakePlayer = null;
        if (FakePlayer.mc.player != null) {
            this._fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.randomUUID(), this.name));
            Command.sendMessage(String.format("%s has been spawned.", this.name));
            this._fakePlayer.func_82149_j(FakePlayer.mc.player);
            this._fakePlayer.field_70759_as = FakePlayer.mc.player.field_70759_as;
            FakePlayer.mc.world.addEntityToWorld(-100, this._fakePlayer);
        }
    }

    @Override
    public void onDisable() {
        if (this._fakePlayer != null) {
            FakePlayer.mc.world.removeEntity(this._fakePlayer);
            this._fakePlayer = null;
        }
    }
}
