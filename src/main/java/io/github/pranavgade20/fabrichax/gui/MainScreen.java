package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.GUI;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
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
            GUI.enabled = false;
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
        for (Map.Entry<Integer, Class> entry : Settings.toggles.entrySet()) {
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
    Class module;
    public ToggleButtonWidget(int x, int y, int width, int height, Class module) throws NoSuchFieldException, IllegalAccessException {
        super(x, y, width, height, Text.of(module.getSimpleName() + "-" + (module.getDeclaredField("enabled").getBoolean(null) ? "ON" : "OFF")));
        this.module = module;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        String text = module.getSimpleName() + "-";
        try {
            module.getMethod("toggle").invoke(null);
            text += module.getDeclaredField("enabled").getBoolean(null) ? "ON" : "OFF";
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            setMessage(Text.of("Couldn't toggle"));
        }
        setMessage(Text.of(text));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        try {
            if (isHovered()) MainScreen.tooltip = Text.of((String) module.getMethod("getToolTip").invoke(null));
            else MainScreen.tooltip = null;
        } catch (Exception e) {
            MainScreen.tooltip = Text.of("Something went wrong");
        }
    }
}
