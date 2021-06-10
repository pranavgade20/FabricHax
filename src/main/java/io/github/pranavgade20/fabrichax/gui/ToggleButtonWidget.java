package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.Hax;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ToggleButtonWidget extends ClickableWidget {
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
