package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.GUI;
import io.github.pranavgade20.fabrichax.Hax;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Comparator;

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

        drawCenteredTextWithShadow(matrices, this.textRenderer, status, this.width / 2, 10, 16777215);

        if (tooltip != null) renderTooltip(matrices, tooltip, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;
        for (Hax<?> category : Settings.categories) {
            int width = this.textRenderer.getWidth(category.getModuleName().replace("Base", "")) + 10;
            if (x + width > this.width) {
                x = 10;
                y += 25;
            }
            addDrawableChild(new MenuButtonWidget(x, y, width, 20, category, textRenderer));
            x += width + 5;

        }
        y += 35;
        int baseY = y;
        x = 10;
        Hax<? extends Base>[] sortedEntries = Settings.toggles.values().toArray(new Hax<?>[0]);
        Arrays.sort(sortedEntries, Comparator.comparing(Hax::getModuleName));
        for (Hax<? extends Base> entry : sortedEntries) {
            try {
                addDrawableChild(new ToggleButtonWidget(x, y, 100, 20, entry, textRenderer));
                y += 25;
                if (y > this.height-20) {
                    x += 110;
                    y = baseY;
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = Text.of("Error initializing modules");
            }
        }
    }
}

