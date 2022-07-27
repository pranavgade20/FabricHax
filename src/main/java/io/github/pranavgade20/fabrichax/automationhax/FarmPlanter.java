package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class FarmPlanter extends AutomationBase {
    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static int count = 0;

    public static FarmPlanter INSTANCE;
    public FarmPlanter() {
        INSTANCE = this;
    }

    public static boolean isPlacable(Block block, Item item) {

        if (block.equals(Blocks.FARMLAND)) {
            return item.equals(Items.WHEAT_SEEDS) ||
                    item.equals(Items.MELON_SEEDS) ||
                    item.equals(Items.PUMPKIN_SEEDS) ||
                    item.equals(Items.BEETROOT_SEEDS) ||
                    item.equals(Items.POTATO) ||
                    item.equals(Items.CARROT);
        } else if (block.equals(Blocks.END_STONE)) {
            return item.equals(Items.CHORUS_FLOWER);
        } else if (block.equals(Blocks.SOUL_SAND)) {
            return item.equals(Items.NETHER_WART);
        }
        return false;
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
                FarmPlanter - plant seeds in farmland around you.

                Configuration information:
                 ~ config FarmPlanter <direction> <size>
                 (to configure shape to be built)
                 where directions include 'up, down, north, south, east, west'
                 for example, use `~ config FarmPlanter left 2`
                 to set this to plant 2 blocks to your left.""";
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
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help FarmPlanter) for more information."), false);
                    return;
                }
            }
            Settings.player.sendMessage(Text.of("~ config FarmPlanter " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help FarmPlanter) for more information."), false);
        }
    }

    public Screen getConfigScreen(Screen parent, String name) {
        return new Screen(Text.of(name)) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                this.renderBackground(matrices);
                drawCenteredText(matrices, this.textRenderer, getTitle(), this.width / 2, 10, 16777215);
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void close() {
                MinecraftClient.getInstance().setScreen(parent);
            }

            @Override
            protected void init() {
                int x = 10;
                int y = 30;
                addDrawableChild(new TextFieldWidget(this.textRenderer, x, y, 100, 20, Text.of("Enabled")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 100, 20, Text.of(String.valueOf(enabled))) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        enabled = !enabled;
                        setMessage(Text.of(String.valueOf(enabled)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Up")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget up = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.up))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.up = FarmPlanter.up == 0 ? 0 : FarmPlanter.up-1;
                        up.setMessage(Text.of(String.valueOf(FarmPlanter.up)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.up = FarmPlanter.up == 8 ? 8 : FarmPlanter.up+1;
                        up.setMessage(Text.of(String.valueOf(FarmPlanter.up)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Down")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget down = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.down))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.down = FarmPlanter.down == 0 ? 0 : FarmPlanter.down-1;
                        down.setMessage(Text.of(String.valueOf(FarmPlanter.down)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.down = FarmPlanter.down == 8 ? 8 : FarmPlanter.down+1;
                        down.setMessage(Text.of(String.valueOf(FarmPlanter.down)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("West")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget west = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.west))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.west = FarmPlanter.west == 0 ? 0 : FarmPlanter.west-1;
                        west.setMessage(Text.of(String.valueOf(FarmPlanter.west)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.west = FarmPlanter.west == 8 ? 8 : FarmPlanter.west+1;
                        west.setMessage(Text.of(String.valueOf(FarmPlanter.west)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("East")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget east = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.east))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.east = FarmPlanter.east == 0 ? 0 : FarmPlanter.east-1;
                        east.setMessage(Text.of(String.valueOf(FarmPlanter.east)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.east = FarmPlanter.east == 8 ? 8 : FarmPlanter.east+1;
                        east.setMessage(Text.of(String.valueOf(FarmPlanter.east)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("North")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget north = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.north = FarmPlanter.north == 0 ? 0 : FarmPlanter.north-1;
                        north.setMessage(Text.of(String.valueOf(FarmPlanter.north)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.north = FarmPlanter.north == 8 ? 8 : FarmPlanter.north+1;
                        north.setMessage(Text.of(String.valueOf(FarmPlanter.north)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("South")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget south = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(FarmPlanter.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.south = FarmPlanter.south == 0 ? 0 : FarmPlanter.south-1;
                        south.setMessage(Text.of(String.valueOf(FarmPlanter.south)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        FarmPlanter.south = FarmPlanter.south == 8 ? 8 : FarmPlanter.south+1;
                        south.setMessage(Text.of(String.valueOf(FarmPlanter.south)));
                    }
                });
            }
        };
    }
}
