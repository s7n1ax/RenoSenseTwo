package me.sjnez.renosense.features.modules.render;

import java.util.Objects;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.event.events.Render3DEvent;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import me.sjnez.renosense.util.DamageUtil;
import me.sjnez.renosense.util.EntityUtil;
import me.sjnez.renosense.util.RenderUtil;
import me.sjnez.renosense.util.RotationUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class Nametags
extends Module {
    private static Nametags INSTANCE = new Nametags();
    private final Setting<Boolean> health = this.register(new Setting<Boolean>("Health", true));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", true));
    private final Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", true));
    private final Setting<Boolean> gamemode = this.register(new Setting<Boolean>("Gamemode", false));
    private final Setting<Boolean> entityID = this.register(new Setting<Boolean>("ID", false));
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> sneak = this.register(new Setting<Boolean>("SneakColor", false));
    private final Setting<Boolean> heldStackName = this.register(new Setting<Boolean>("StackName", false));
    private final Setting<Boolean> whiter = this.register(new Setting<Boolean>("White", false));
    private final Setting<Boolean> onlyFov = this.register(new Setting<Boolean>("OnlyFov", false));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.valueOf(false), v -> this.scaleing.getValue()));

    public Nametags() {
        super("NameTags", "See other players names and other info about them.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Nametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Nametags();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!Nametags.fullNullCheck()) {
            for (EntityPlayer player : Nametags.mc.world.field_73010_i) {
                if (player == null || player.equals(Nametags.mc.player) || !player.func_70089_S() || player.func_82150_aj() && !this.invisibles.getValue().booleanValue() || this.onlyFov.getValue().booleanValue() && !RotationUtil.isInFov(player)) continue;
                double x = this.interpolate(player.field_70142_S, player.field_70165_t, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX;
                double y = this.interpolate(player.field_70137_T, player.field_70163_u, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY;
                double z = this.interpolate(player.field_70136_U, player.field_70161_v, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y, z, event.getPartialTicks());
            }
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.func_70093_af() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + Nametags.mc.getRenderManager().viewerPosX, y + Nametags.mc.getRenderManager().viewerPosY, z + Nametags.mc.getRenderManager().viewerPosZ);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double)this.scaling.getValue().floatValue() * (distance * (double)this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double)this.scaling.getValue().floatValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-Nametags.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Nametags.mc.getRenderManager().playerViewX, (float)(Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue().booleanValue()) {
            RenderUtil.drawRect(-width - 2, -(this.renderer.getFontHeight() + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.disableBlend();
        ItemStack renderMainHand = player.func_184614_ca().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        if (this.heldStackName.getValue().booleanValue() && !renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            String stackName = renderMainHand.getDisplayName();
            int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(this.getBiggestArmorTag(player) + 20.0f), -1);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.getValue().booleanValue()) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == null) continue;
                xOffset -= 8;
            }
            xOffset -= 8;
            ItemStack renderOffhand = player.func_184592_cb().copy();
            if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }
            this.renderItemStack(renderOffhand, xOffset, -26);
            xOffset += 16;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == null) continue;
                ItemStack armourStack = stack.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                this.renderItemStack(armourStack, xOffset, -26);
                xOffset += 16;
            }
            this.renderItemStack(renderMainHand, xOffset, -26);
            GlStateManager.popMatrix();
        }
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, stack, x, y);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y - 8;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            short level = enchants.getCompoundTagAt(index).getShort("lvl");
            Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc == null) continue;
            String encName = enc.isCurse() ? TextFormatting.RED + enc.getTranslatedName(level).substring(11).substring(0, 1).toLowerCase() : enc.getTranslatedName(level).substring(0, 1).toLowerCase();
            encName = encName + level;
            this.renderer.drawStringWithShadow(encName, x * 2, enchantmentY, -1);
            enchantmentY -= 8;
        }
        if (DamageUtil.hasDurability(stack)) {
            int percent = DamageUtil.getRoundedDamage(stack);
            String color = percent >= 60 ? "§a" : (percent >= 25 ? "§e" : "§c");
            this.renderer.drawStringWithShadow(color + percent + "%", x * 2, enchantmentY, -1);
        }
    }

    private float getBiggestArmorTag(EntityPlayer player) {
        ItemStack renderOffHand;
        Enchantment enc;
        short id;
        int index;
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                NBTTagList enchants = stack.getEnchantmentTagList();
                for (index = 0; index < enchants.tagCount(); ++index) {
                    id = enchants.getCompoundTagAt(index).getShort("id");
                    enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc == null) continue;
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (!(encY > enchantmentY)) continue;
            enchantmentY = encY;
        }
        ItemStack renderMainHand = player.func_184614_ca().copy();
        if (renderMainHand.hasEffect()) {
            float encY = 0.0f;
            NBTTagList enchants = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants.tagCount(); ++index2) {
                id = enchants.getCompoundTagAt(index2).getShort("id");
                Enchantment enc2 = Enchantment.getEnchantmentByID((int)id);
                if (enc2 == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        if ((renderOffHand = player.func_184592_cb().copy()).hasEffect()) {
            float encY = 0.0f;
            NBTTagList enchants = renderOffHand.getEnchantmentTagList();
            for (index = 0; index < enchants.tagCount(); ++index) {
                short id2 = enchants.getCompoundTagAt(index).getShort("id");
                enc = Enchantment.getEnchantmentByID((int)id2);
                if (enc == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (float)(arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue().booleanValue()) {
            return name;
        }
        float health = EntityUtil.getHealth(player);
        String color = health > 18.0f ? "§a" : (health > 16.0f ? "§2" : (health > 12.0f ? "§e" : (health > 8.0f ? "§6" : (health > 5.0f ? "§c" : "§4"))));
        String pingStr = "";
        if (this.ping.getValue().booleanValue()) {
            try {
                int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.func_110124_au()).getResponseTime();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception responseTime) {
                // empty catch block
            }
        }
        String popStr = " ";
        if (this.totemPops.getValue().booleanValue()) {
            popStr = popStr + RenoSense.totemPopManager.getTotemPopString(player);
        }
        String idString = "";
        if (this.entityID.getValue().booleanValue()) {
            idString = idString + "ID: " + player.func_145782_y() + " ";
        }
        String gameModeStr = "";
        if (this.gamemode.getValue().booleanValue()) {
            String string = player.isCreative() ? gameModeStr + "[C] " : (gameModeStr = player.isSpectator() || player.func_82150_aj() ? gameModeStr + "[I] " : gameModeStr + "[S] ");
        }
        name = Math.floor(health) == (double)health ? name + color + " " + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead") : name + color + " " + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        return pingStr + idString + gameModeStr + name + popStr;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;
        if (this.whiter.getValue().booleanValue()) {
            colour = -1;
        }
        if (RenoSense.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.func_82150_aj()) {
            colour = -1113785;
        } else if (player.func_70093_af() && this.sneak.getValue().booleanValue()) {
            colour = -6481515;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }
}
