package io.github.pranavgade20.fabrichax.scripting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class ScriptScreen extends Screen {
    final int LINE_HEIGHT = 10;
    final int SCRIPT_X_OFFSET = 10;
    final int SCRIPT_Y_OFFSET = 30;

    Screen parent;
    ScriptBase scriptBase;

    Pair<Integer, Integer> cursor = new Pair<>(0, 0);
    int tickCounter = 0;
    protected ScriptScreen(Text title, Screen parent, ScriptBase scriptBase) {
        super(title);
        this.parent = parent;
        this.scriptBase = scriptBase;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        int x = 10;
        int y = 30;
        for (String str : scriptBase.getScript()) {
            this.textRenderer.draw(matrices, str, x, y, -1);
            y += LINE_HEIGHT;
        }

        if (tickCounter / 6 % 2 == 0) {
            int cursor_x = this.textRenderer.getWidth(scriptBase.getScript().get(cursor.getRight()).substring(0, cursor.getLeft())) + SCRIPT_X_OFFSET;
            int cursor_y = cursor.getRight() * LINE_HEIGHT + SCRIPT_Y_OFFSET;

            DrawableHelper.fill(matrices, cursor_x, cursor_y, cursor_x + 1, cursor_y + 9, 0xFFEEEEEE);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(parent);
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.tickCounter;
    }
}
