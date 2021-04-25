package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.Hax;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MenuButtonWidget extends AbstractButtonWidget {
    Hax<? extends Base> module;

    public MenuButtonWidget(int x, int y, int width, int height, Hax<? extends Base> module) {
        super(x, y, width, height, Text.of(module.getModuleName().replace("Base", "")));
        this.module = module;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        MinecraftClient.getInstance().openScreen(new CategoryScreen(module));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (isHovered()) MainScreen.tooltip = Text.of(module.getToolTip());
        else MainScreen.tooltip = null;
    }
}
