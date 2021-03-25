package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class Walker extends AutomationBase {
    public static double speed = 5;

    public static Walker INSTANCE;
    public Walker() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Walker - walk in the direction you are looking at.\n" +
                "This is most effective on the nether roof\n" +
                "Configuration information:\n" +
                " ~ config Walker speed <speed>\n" +
                " where <speed> is the speed you want to move with.\n";
    }

    @Override
    public String getToolTip() {
        return "Walk in the direction you are looking at.";
    }

    @Override
    public HashMap<String, String> getArgs() {
        HashMap<String, String> ret = super.getArgs();
        ret.put("speed", String.valueOf(speed));
        return ret;
    }

    @Override
    public void setArgs(HashMap<String, String> args) {
        super.setArgs(args);

        speed = Double.parseDouble(args.get("speed"));
    }

    @Override
    public void config(String params) {
        try {
            String parameter = params.split(" ")[1].toLowerCase();
            if ("speed".startsWith(parameter)) {
                speed = Double.parseDouble(params.split(" ")[2]);
                Settings.player.sendMessage(Text.of("Speed updated to: " + speed), false);
            } else {
                Settings.player.sendMessage(Text.of("Unidentified parameter" + parameter), false);
            }
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help template) for more information."), false);
        }
    }

    @Override
    public Screen getConfigScreen(Screen parent, String name) {
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
                int x = 10;
                int y = 30;
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

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Speed")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget speed = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(Walker.speed))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Walker.speed = Walker.speed == 0 ? 0 : Walker.speed-1;
                        speed.setMessage(Text.of(String.valueOf(Walker.speed)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Walker.speed = Walker.speed+1;
                        speed.setMessage(Text.of(String.valueOf(Walker.speed)));
                    }
                });
            }
        };
    }
}
