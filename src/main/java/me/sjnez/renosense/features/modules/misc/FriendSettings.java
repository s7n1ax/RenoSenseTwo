package me.sjnez.renosense.features.modules.misc;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;

public class FriendSettings
extends Module {
    private static FriendSettings INSTANCE;
    public Setting<Boolean> notify = this.register(new Setting<Boolean>("Notify", false));

    public FriendSettings() {
        super("FriendSettings", "When Notify is on, it sends a message the person you add as a friend on the client.", Module.Category.MISC, true, false, false);
        INSTANCE = this;
    }

    public static FriendSettings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FriendSettings();
        }
        return INSTANCE;
    }
}
