package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.Hax;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;

public class MenuButtonWidget extends PressableTextWidget {
    Hax<? extends Base> module;

    public MenuButtonWidget(int x, int y, int width, int height, Hax<? extends Base> module, TextRenderer textRenderer) {
        super(x, y, width, height, Text.of(module.getModuleName().replace("Base", "")), button -> {
        }, textRenderer);
        this.module = module;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        MinecraftClient.getInstance().setScreen(new CategoryScreen(module));
    }

    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (isHovered()) MainScreen.tooltip = Text.of(module.getToolTip());
        else MainScreen.tooltip = null;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
