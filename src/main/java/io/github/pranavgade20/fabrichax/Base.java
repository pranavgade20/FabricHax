package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class Base {
    public boolean enabled = false;

    public boolean toggle() {
        enabled = !enabled;
        return true;
    }

    public String getHelpMessage() {
        // this either means that the subclass does not override this method, or something happened in Hax.of()
        return "Uh oh - this should not be happening. Please send me a bug report if you can :)";
    }

    public String getToolTip() {
        // this either means that the subclass does not override this method, or something happened in Hax.of()
        return "Uh oh - this should not be happening. Please send me a bug report if you can :)";
    }

    public HashMap<String, String> getArgs() {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("enabled", String.valueOf(enabled));
        return ret;
    }

    public void setArgs(HashMap<String, String> args) { }

    public void config(String params) {
        Settings.player.sendMessage(Text.of("You cannot configure this module."),false);
    }

    public Screen getConfigScreen(Screen parent, String name) {
        return new Screen(Text.of(name)) {
            @Override
            public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                this.renderBackground(matrices, mouseX, mouseY, delta);
                matrices.drawCenteredTextWithShadow(this.textRenderer, getTitle(), this.width / 2, 10, 16777215);
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void close() {
                MinecraftClient.getInstance().setScreen(parent);
            }

            @Override
            public void init() {
                int x = 10;
                int y = 30;
                addDrawableChild(new TextFieldWidget(this.textRenderer, x, y, 100, 20, Text.of("Enabled")) {
                    @Override
                    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        matrices.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ButtonWidget.Builder(Text.of(String.valueOf(enabled)), (button) -> {
                    enabled = !enabled;
                    button.setMessage(Text.of(String.valueOf(enabled)));
                }).position(x + 110, y).size(100, 20).build());
            }
        };
    }
}
