package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

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

    public void config(String params) {
        Settings.player.sendMessage(Text.of("You cannot configure this module."),false);
    }

    public Screen getConfigScreen(net.minecraft.client.gui.screen.Screen parent, String name) {
        return new Screen(Text.of(name)) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                this.renderBackground(matrices);
                drawCenteredText(matrices, this.textRenderer, getTitle(), this.width / 2, 10, 16777215);
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void onClose() {
                MinecraftClient.getInstance().openScreen(parent);
            }

            @Override
            protected void init() {
                addButton(new TextFieldWidget(this.textRenderer, x, y, 100, 20, Text.of("Enabled")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 100, 20, Text.of(String.valueOf(enabled))) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        enabled = !enabled;
                        setMessage(Text.of(String.valueOf(enabled)));
                    }
                });
                y+=25;
            }
        };
    }

    public class Screen extends net.minecraft.client.gui.screen.Screen {
        public int x,y;
        public TextRenderer textRenderer;
        protected Screen(Text title) {
            super(title);
            x = 10;
            y = 30;
        }

        public <T extends AbstractButtonWidget> T addButton(T button) {
            return super.addButton(button);
        }

        @Override
        public void init(MinecraftClient client, int width, int height) {
            super.init(client, width, height);
            textRenderer = super.textRenderer;
        }
    }
}
