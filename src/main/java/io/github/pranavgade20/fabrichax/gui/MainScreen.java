package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.GUI;
import io.github.pranavgade20.fabrichax.Hax;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Map;

public class MainScreen extends Screen {
    static Text tooltip = null;
    static Text status = Text.of("FabricHax");
    public MainScreen(Text title) {
        super(title);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            GUI.INSTANCE.enabled = false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, status, this.width / 2, 10, 16777215);

        if (tooltip != null) renderTooltip(matrices, tooltip, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;
        for (Map.Entry<Integer, Hax<?>> entry : Settings.toggles.entrySet()) {
            try {
                addButton(new ToggleButtonWidget(x, y, 100, 20, entry.getValue()));
                y += 25;
                if (y > this.height) {
                    x += 110;
                    y = 30;
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = Text.of("Error initializing modules");
            }
        }
    }
}

class ToggleButtonWidget extends AbstractButtonWidget {
    Hax<? extends Base> module;
    public ToggleButtonWidget(int x, int y, int width, int height, Hax<? extends Base> module) {
        super(x, y, width, height, Text.of(module.getModuleName() + "-" + (module.isEnabled() ? "ON" : "OFF")));
        this.module = module;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        module.toggle();
        setMessage(Text.of(module.getModuleName() + "-" + (module.isEnabled() ? "ON" : "OFF")));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (isHovered()) MainScreen.tooltip = Text.of(module.getToolTip());
        else MainScreen.tooltip = null;
    }
}
