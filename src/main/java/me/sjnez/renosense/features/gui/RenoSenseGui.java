package me.sjnez.renosense.features.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import me.sjnez.renosense.RenoSense;
import me.sjnez.renosense.features.Feature;
import me.sjnez.renosense.features.gui.components.Component;
import me.sjnez.renosense.features.gui.components.Snow;
import me.sjnez.renosense.features.gui.components.items.DescBox;
import me.sjnez.renosense.features.gui.components.items.Item;
import me.sjnez.renosense.features.gui.components.items.buttons.ModuleButton;
import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class RenoSenseGui
extends GuiScreen {
    private static RenoSenseGui INSTANCE = new RenoSenseGui();
    private final ArrayList<Component> components = new ArrayList();
    private final DescBox descBox = new DescBox("Description", 500, 20, true);
    private ArrayList<Snow> _snowList = new ArrayList();

    public RenoSenseGui() {
        this.setInstance();
        this.load();
    }

    public static RenoSenseGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RenoSenseGui();
        }
        return INSTANCE;
    }

    public static RenoSenseGui getClickGui() {
        return RenoSenseGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -80;
        Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this._snowList.add(snow);
            }
        }
        for (final Module.Category category : RenoSense.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 110, 4, true){

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    RenoSense.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton((Module)module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        if (ClickGui.getInstance().tint.getValue().booleanValue()) {
            this.func_73733_a(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        this.descBox.drag(mouseX, mouseY);
        this.descBox.drawScreen(mouseX, mouseY);
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        ScaledResolution res = new ScaledResolution(this.mc);
        if (!this._snowList.isEmpty() && ClickGui.getInstance().snowing.getValue().booleanValue()) {
            this._snowList.forEach(snow -> snow.Update(res));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.descBox.mouseClicked(mouseX, mouseY, clickedButton);
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.descBox.mouseReleased(mouseX, mouseY, releaseButton);
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            DescBox.descY -= 10.0f;
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            DescBox.descY += 10.0f;
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}
