package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class SpawnProofer extends AutomationBase{
    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static int count = 0;

    public static SpawnProofer INSTANCE;
    public SpawnProofer() {
        INSTANCE = this;
    }

    public String getHelpMessage() {
        return "SpawnProofer - spawnproof your surroundings with redstone components\n" +
                "\nConfiguration information:\n" +
                " ~ config SpawnProofer <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'up, down, north, south, east, west'\n" +
                " for example, use `~ config SpawnProofer left 2`\n" +
                " to set this to spawnproof 2 blocks to your left.";
    }
    @Override
    public void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            switch (direction) {
                case "up":
                    up = size + 1;
                    break;
                case "down":
                    down = size;
                    break;
                case "north":
                    north = size;
                    break;
                case "south":
                    south = size;
                    break;
                case "east":
                    east = size;
                    break;
                case "west":
                    west = size;
                    break;
                default:
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help SpawnProofer) for more information."), false);
                    return;
            }
            Settings.player.sendMessage(Text.of("~ config SpawnProofer " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help SpawnProofer) for more information."), false);
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

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Up")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget up = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.up))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.up = SpawnProofer.up == 0 ? 0 : SpawnProofer.up-1;
                        up.setMessage(Text.of(String.valueOf(SpawnProofer.up)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.up = SpawnProofer.up == 8 ? 8 : SpawnProofer.up+1;
                        up.setMessage(Text.of(String.valueOf(SpawnProofer.up)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Down")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget down = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.down))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.down = SpawnProofer.down == 0 ? 0 : SpawnProofer.down-1;
                        down.setMessage(Text.of(String.valueOf(SpawnProofer.down)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.down = SpawnProofer.down == 8 ? 8 : SpawnProofer.down+1;
                        down.setMessage(Text.of(String.valueOf(SpawnProofer.down)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("West")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget west = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.west))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.west = SpawnProofer.west == 0 ? 0 : SpawnProofer.west-1;
                        west.setMessage(Text.of(String.valueOf(SpawnProofer.west)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.west = SpawnProofer.west == 8 ? 8 : SpawnProofer.west+1;
                        west.setMessage(Text.of(String.valueOf(SpawnProofer.west)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("East")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget east = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.east))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.east = SpawnProofer.east == 0 ? 0 : SpawnProofer.east-1;
                        east.setMessage(Text.of(String.valueOf(SpawnProofer.east)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.east = SpawnProofer.east == 8 ? 8 : SpawnProofer.east+1;
                        east.setMessage(Text.of(String.valueOf(SpawnProofer.east)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("North")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget north = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.north = SpawnProofer.north == 0 ? 0 : SpawnProofer.north-1;
                        north.setMessage(Text.of(String.valueOf(SpawnProofer.north)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.north = SpawnProofer.north == 8 ? 8 : SpawnProofer.north+1;
                        north.setMessage(Text.of(String.valueOf(SpawnProofer.north)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("South")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget south = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(SpawnProofer.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.south = SpawnProofer.south == 0 ? 0 : SpawnProofer.south-1;
                        south.setMessage(Text.of(String.valueOf(SpawnProofer.south)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        SpawnProofer.south = SpawnProofer.south == 8 ? 8 : SpawnProofer.south+1;
                        south.setMessage(Text.of(String.valueOf(SpawnProofer.south)));
                    }
                });
            }
        };
    }
}
