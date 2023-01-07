package me.sjnez.renosense.features.modules.player;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;

public class NoEntityTrace
extends Module {
    public static NoEntityTrace INSTANCE;
    public Setting<Boolean> pickaxe = this.register(new Setting<Boolean>("Pickaxe", true));

    public NoEntityTrace() {
        super("NoEntityTrace", "Place and break blocks through entities.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoEntityTrace getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoEntityTrace();
        }
        return INSTANCE;
    }
}
