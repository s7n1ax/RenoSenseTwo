package me.sjnez.renosense.features.modules.movement;

import me.sjnez.renosense.event.events.PacketEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow
extends Module {
    public Setting<Boolean> noSlow = this.register(new Setting<Boolean>("NoSlow", true));
    public Setting<Boolean> explosions = this.register(new Setting<Boolean>("Explosions", false));
    public Setting<Float> horizontal = this.register(new Setting<Float>("Horizontal", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.explosions.getValue()));
    public Setting<Float> vertical = this.register(new Setting<Float>("Vertical", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.explosions.getValue()));
    public Setting<Boolean> inventoryMove = this.register(new Setting<Boolean>("Inventory Move", true));
    private static NoSlow INSTANCE = new NoSlow();
    private boolean sneaking = false;
    private static KeyBinding[] keys = new KeyBinding[]{NoSlow.mc.gameSettings.keyBindForward, NoSlow.mc.gameSettings.keyBindBack, NoSlow.mc.gameSettings.keyBindLeft, NoSlow.mc.gameSettings.keyBindRight, NoSlow.mc.gameSettings.keyBindJump, NoSlow.mc.gameSettings.keyBindSprint};

    public NoSlow() {
        super("NoSlow", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoSlow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlow();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getStage() == 0 && NoSlow.mc.player != null) {
            Object velocity;
            if (event.getPacket() instanceof SPacketEntityVelocity && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == NoSlow.mc.player.field_145783_c) {
                if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                ((SPacketEntityVelocity)velocity).motionX = (int)((float)((SPacketEntityVelocity)velocity).motionX * this.horizontal.getValue().floatValue());
                ((SPacketEntityVelocity)velocity).motionY = (int)((float)((SPacketEntityVelocity)velocity).motionY * this.vertical.getValue().floatValue());
                ((SPacketEntityVelocity)velocity).motionZ = (int)((float)((SPacketEntityVelocity)velocity).motionZ * this.horizontal.getValue().floatValue());
            }
            if (this.explosions.getValue().booleanValue() && event.getPacket() instanceof SPacketExplosion) {
                velocity = (SPacketExplosion)event.getPacket();
                ((SPacketExplosion)velocity).motionX *= this.horizontal.getValue().floatValue();
                ((SPacketExplosion)velocity).motionY *= this.vertical.getValue().floatValue();
                ((SPacketExplosion)velocity).motionZ *= this.horizontal.getValue().floatValue();
            }
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (NoSlow.nullCheck()) {
            return;
        }
        if (this.inventoryMove.getValue().booleanValue() && NoSlow.mc.currentScreen != null) {
            NoSlow.mc.player.movementInput.moveStrafe = 0.0f;
            NoSlow.mc.player.movementInput.moveForward = 0.0f;
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindForward.getKeyCode(), (boolean)Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindForward.getKeyCode()));
            if (Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindForward.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveForward += 1.0f;
                NoSlow.mc.player.movementInput.forwardKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.forwardKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindBack.getKeyCode(), (boolean)Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindBack.getKeyCode()));
            if (Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindBack.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveForward -= 1.0f;
                NoSlow.mc.player.movementInput.backKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.backKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindLeft.getKeyCode(), (boolean)Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindLeft.getKeyCode()));
            if (Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindLeft.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveStrafe += 1.0f;
                NoSlow.mc.player.movementInput.leftKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.leftKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindRight.getKeyCode(), (boolean)Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindRight.getKeyCode()));
            if (Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindRight.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveStrafe -= 1.0f;
                NoSlow.mc.player.movementInput.rightKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.rightKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindJump.getKeyCode()));
            NoSlow.mc.player.movementInput.jump = Keyboard.isKeyDown(NoSlow.mc.gameSettings.keyBindJump.getKeyCode());
        }
        if (this.noSlow.getValue().booleanValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.func_184218_aH()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }
}
