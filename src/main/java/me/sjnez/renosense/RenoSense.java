package me.sjnez.renosense;

import java.io.InputStream;
import java.nio.ByteBuffer;
import me.sjnez.renosense.manager.ColorManager;
import me.sjnez.renosense.manager.CommandManager;
import me.sjnez.renosense.manager.ConfigManager;
import me.sjnez.renosense.manager.EventManager;
import me.sjnez.renosense.manager.FileManager;
import me.sjnez.renosense.manager.FriendManager;
import me.sjnez.renosense.manager.HoleManager;
import me.sjnez.renosense.manager.InventoryManager;
import me.sjnez.renosense.manager.ModuleManager;
import me.sjnez.renosense.manager.PacketManager;
import me.sjnez.renosense.manager.PositionManager;
import me.sjnez.renosense.manager.PotionManager;
import me.sjnez.renosense.manager.ReloadManager;
import me.sjnez.renosense.manager.RotationManager;
import me.sjnez.renosense.manager.ServerManager;
import me.sjnez.renosense.manager.SpeedManager;
import me.sjnez.renosense.manager.TextManager;
import me.sjnez.renosense.manager.TimerManager;
import me.sjnez.renosense.manager.TotemPopManager;
import me.sjnez.renosense.util.IconUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="renosense", name="RenoSense", version="0.0.3")
public class RenoSense {
    public static final String MODID = "renosense";
    public static final String MODNAME = "RenoSense";
    public static final String MODVER = "0.0.3";
    public static final Logger LOGGER = LogManager.getLogger("RenoSense 2");
    public static TotemPopManager totemPopManager;
    public static TimerManager timerManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    @Mod.Instance
    public static RenoSense INSTANCE;
    private static boolean unloaded;

    public static void load() {
        LOGGER.info("\n\nLoading RenoSense 2 by Sjnez");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        totemPopManager = new TotemPopManager();
        timerManager = new TimerManager();
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("RenoSense 2 successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading RenoSense 2 by Sjnez");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        RenoSense.onUnload();
        timerManager = null;
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("RenoSense 2 unloaded!\n");
    }

    public static void reload() {
        RenoSense.unload(false);
        RenoSense.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(RenoSense.configManager.config.replaceFirst("renosense/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("RENOSENSE!!!");
    }

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/renosense/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/renosense/icons/icon-32x.png");){
                ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            }
            catch (Exception e) {
                LOGGER.error("Couldn't set Windows Icon", e);
            }
        }
    }

    private void setWindowsIcon() {
        RenoSense.setWindowIcon();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("RenoSense 2 0.0.3");
        this.setWindowsIcon();
        RenoSense.load();
    }

    static {
        unloaded = false;
    }
}
