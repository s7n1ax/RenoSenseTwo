package me.sjnez.renosense.features.modules.client;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;

public class ModuleTools
extends Module {
    private static ModuleTools INSTANCE;
    public Setting<Notifier> notifier = this.register(new Setting<Notifier>("ModuleNotifier", Notifier.FUTURE));

    public ModuleTools() {
        super("ModuleTools", "Change settings for Pop Notifier and Module Notifier.", Module.Category.CLIENT, true, false, false);
        INSTANCE = this;
    }

    public static ModuleTools getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleTools();
        }
        return INSTANCE;
    }

    public static enum Notifier {
        TROLLGOD,
        PHOBOS,
        FUTURE,
        DOTGOD;

    }
}
