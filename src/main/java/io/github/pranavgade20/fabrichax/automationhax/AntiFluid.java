package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class AntiFluid extends AutomationBase {
    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static AntiFluid INSTANCE;
    public AntiFluid() {
        INSTANCE = this;
    }
    public String getHelpMessage() {
        return "AntiFluid - replace fluids around you with blocks\n" +
                "\nConfiguration information:\n" +
                " ~ config AntiFluid <direction> <size>\n" +
                " (to configure shape to be filled)\n" +
                " where directions include 'up, down, north, south, east, west'\n" +
                " for example, use `~ config AntiFluid up 2`\n" +
                " to set this to fill 2 blocks above you.";
    }

    @Override
    public HashMap<String, String> getArgs() {
        HashMap<String, String> ret = super.getArgs();
        ret.put("up", String.valueOf(up));
        ret.put("down", String.valueOf(down));
        ret.put("north", String.valueOf(north));
        ret.put("south", String.valueOf(south));
        ret.put("east", String.valueOf(east));
        ret.put("west", String.valueOf(west));
        return ret;
    }

    @Override
    public void setArgs(HashMap<String, String> args) {
        super.setArgs(args);

        up = Integer.parseInt(args.get("up"));
        down = Integer.parseInt(args.get("down"));
        north = Integer.parseInt(args.get("north"));
        south = Integer.parseInt(args.get("south"));
        east = Integer.parseInt(args.get("east"));
        west = Integer.parseInt(args.get("west"));
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
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), false);
                    return;
            }
            Settings.player.sendMessage(Text.of("~ config AntiFluid " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), false);
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
                addButton(new ClickableWidget(x+110, y, 100, 20, Text.of(String.valueOf(enabled))) {
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
                final TextFieldWidget up = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.up))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.up = AntiFluid.up == 0 ? 0 : AntiFluid.up-1;
                        up.setMessage(Text.of(String.valueOf(AntiFluid.up)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.up = AntiFluid.up == 8 ? 8 : AntiFluid.up+1;
                        up.setMessage(Text.of(String.valueOf(AntiFluid.up)));
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
                final TextFieldWidget down = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.down))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.down = AntiFluid.down == 0 ? 0 : AntiFluid.down-1;
                        down.setMessage(Text.of(String.valueOf(AntiFluid.down)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.down = AntiFluid.down == 8 ? 8 : AntiFluid.down+1;
                        down.setMessage(Text.of(String.valueOf(AntiFluid.down)));
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
                final TextFieldWidget west = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.west))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.west = AntiFluid.west == 0 ? 0 : AntiFluid.west-1;
                        west.setMessage(Text.of(String.valueOf(AntiFluid.west)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.west = AntiFluid.west == 8 ? 8 : AntiFluid.west+1;
                        west.setMessage(Text.of(String.valueOf(AntiFluid.west)));
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
                final TextFieldWidget east = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.east))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.east = AntiFluid.east == 0 ? 0 : AntiFluid.east-1;
                        east.setMessage(Text.of(String.valueOf(AntiFluid.east)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.east = AntiFluid.east == 8 ? 8 : AntiFluid.east+1;
                        east.setMessage(Text.of(String.valueOf(AntiFluid.east)));
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
                final TextFieldWidget north = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.north = AntiFluid.north == 0 ? 0 : AntiFluid.north-1;
                        north.setMessage(Text.of(String.valueOf(AntiFluid.north)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.north = AntiFluid.north == 8 ? 8 : AntiFluid.north+1;
                        north.setMessage(Text.of(String.valueOf(AntiFluid.north)));
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
                final TextFieldWidget south = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AntiFluid.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.south = AntiFluid.south == 0 ? 0 : AntiFluid.south-1;
                        south.setMessage(Text.of(String.valueOf(AntiFluid.south)));
                    }
                });
                addButton(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AntiFluid.south = AntiFluid.south == 8 ? 8 : AntiFluid.south+1;
                        south.setMessage(Text.of(String.valueOf(AntiFluid.south)));
                    }
                });
            }
        };
    }
}
