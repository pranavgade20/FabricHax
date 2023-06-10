package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AnimalFeeder extends AutomationBase {
    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static int count = 0;

    public static Set<LivingEntity> surrounding = new HashSet<>();
    public static Set<LivingEntity> toFeed = new HashSet<>();

    public static AnimalFeeder INSTANCE;
    public AnimalFeeder() {
        INSTANCE = this;
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

    public String getHelpMessage() {
        return """
                AnimalFeeder - feed animals around you.

                Configuration information:
                 ~ config AnimalFeeder <direction> <size>
                 (to configure shape)
                 where directions include 'up, down, north, south, east, west'
                 for example, use `~ config AnimalFeeder left 2`
                 to set this to feed animals up to 2 blocks to your left.""";
    }
    @Override
    public void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            switch (direction) {
                case "up" -> up = size + 1;
                case "down" -> down = size;
                case "north" -> north = size;
                case "south" -> south = size;
                case "east" -> east = size;
                case "west" -> west = size;
                default -> {
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AnimalFeeder) for more information."), false);
                    return;
                }
            }
            Settings.player.sendMessage(Text.of("~ config AnimalFeeder " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AnimalFeeder) for more information."), false);
        }
    }

    public Screen getConfigScreen(Screen parent, String name) {
        return new Screen(Text.of(name)) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                this.renderBackground(matrices);
                drawCenteredTextWithShadow(matrices, this.textRenderer, getTitle(), this.width / 2, 10, 16777215);
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
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 100, 20, Text.of(String.valueOf(enabled)), button -> {
                    enabled = !enabled;
                    button.setMessage(Text.of(String.valueOf(enabled)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Up")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget up = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.up))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.up = AnimalFeeder.up == 0 ? 0 : AnimalFeeder.up - 1;
                    up.setMessage(Text.of(String.valueOf(AnimalFeeder.up)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.up = AnimalFeeder.up == 8 ? 8 : AnimalFeeder.up + 1;
                    up.setMessage(Text.of(String.valueOf(AnimalFeeder.up)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Down")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget down = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.down))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.down = AnimalFeeder.down == 0 ? 0 : AnimalFeeder.down - 1;
                    down.setMessage(Text.of(String.valueOf(AnimalFeeder.down)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.down = AnimalFeeder.down == 8 ? 8 : AnimalFeeder.down + 1;
                    down.setMessage(Text.of(String.valueOf(AnimalFeeder.down)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("West")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget west = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.west))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.west = AnimalFeeder.west == 0 ? 0 : AnimalFeeder.west - 1;
                    west.setMessage(Text.of(String.valueOf(AnimalFeeder.west)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.west = AnimalFeeder.west == 8 ? 8 : AnimalFeeder.west + 1;
                    west.setMessage(Text.of(String.valueOf(AnimalFeeder.west)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("East")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget east = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.east))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.east = AnimalFeeder.east == 0 ? 0 : AnimalFeeder.east - 1;
                    east.setMessage(Text.of(String.valueOf(AnimalFeeder.east)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.east = AnimalFeeder.east == 8 ? 8 : AnimalFeeder.east + 1;
                    east.setMessage(Text.of(String.valueOf(AnimalFeeder.east)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("North")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget north = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.north = AnimalFeeder.north == 0 ? 0 : AnimalFeeder.north - 1;
                    north.setMessage(Text.of(String.valueOf(AnimalFeeder.north)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.north = AnimalFeeder.north == 8 ? 8 : AnimalFeeder.north + 1;
                    north.setMessage(Text.of(String.valueOf(AnimalFeeder.north)));
                }, textRenderer));

                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("South")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget south = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AnimalFeeder.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AnimalFeeder.south = AnimalFeeder.south == 0 ? 0 : AnimalFeeder.south - 1;
                    south.setMessage(Text.of(String.valueOf(AnimalFeeder.south)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AnimalFeeder.south = AnimalFeeder.south == 8 ? 8 : AnimalFeeder.south + 1;
                    south.setMessage(Text.of(String.valueOf(AnimalFeeder.south)));
                }, textRenderer));

            }
        };
    }
}
