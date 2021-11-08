package io.github.pranavgade20.fabrichax.automationhax;

import com.mojang.authlib.GameProfile;
import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.gui.MainScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AutoMiner extends AutomationBase {
    public static int up = 3;
    public static int down = 0;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static int count = 0;

    public static HashSet<Item> mineable = new HashSet<>();

    static {
        mineable.add(Blocks.STONE.asItem());
        mineable.add(Blocks.IRON_ORE.asItem());
    }

    public static AutoMiner INSTANCE;
    public AutoMiner() {
        INSTANCE = this;
    }

    public String getHelpMessage() {
        return "AutoMiner - automatically mine blocks around you\n" +
                "\nConfiguration information:\n" +
                " ~ config AutoMiner <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'up, down, north, south, east, west'\n" +
                " for example, use `~ config AutoMiner left 2`\n" +
                " to set this to plant 2 blocks to your left.";
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
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AutoMiner) for more information."), false);
                    return;
            }
            Settings.player.sendMessage(Text.of("~ config AutoMiner " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AutoMiner) for more information."), false);
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
            public void onClose() {
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
                final TextFieldWidget up = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.up))) {
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
                        AutoMiner.up = AutoMiner.up == 0 ? 0 : AutoMiner.up-1;
                        up.setMessage(Text.of(String.valueOf(AutoMiner.up)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.up = AutoMiner.up == 8 ? 8 : AutoMiner.up+1;
                        up.setMessage(Text.of(String.valueOf(AutoMiner.up)));
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
                final TextFieldWidget down = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.down))) {
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
                        AutoMiner.down = AutoMiner.down == 0 ? 0 : AutoMiner.down-1;
                        down.setMessage(Text.of(String.valueOf(AutoMiner.down)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.down = AutoMiner.down == 8 ? 8 : AutoMiner.down+1;
                        down.setMessage(Text.of(String.valueOf(AutoMiner.down)));
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
                final TextFieldWidget west = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.west))) {
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
                        AutoMiner.west = AutoMiner.west == 0 ? 0 : AutoMiner.west-1;
                        west.setMessage(Text.of(String.valueOf(AutoMiner.west)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.west = AutoMiner.west == 8 ? 8 : AutoMiner.west+1;
                        west.setMessage(Text.of(String.valueOf(AutoMiner.west)));
                    }
                });
                y+=25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("East")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget east = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.east))) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
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
                        AutoMiner.east = AutoMiner.east == 0 ? 0 : AutoMiner.east-1;
                        east.setMessage(Text.of(String.valueOf(AutoMiner.east)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.east = AutoMiner.east == 8 ? 8 : AutoMiner.east+1;
                        east.setMessage(Text.of(String.valueOf(AutoMiner.east)));
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
                final TextFieldWidget north = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void appendNarrations(NarrationMessageBuilder builder) {

                    }

                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.north = AutoMiner.north == 0 ? 0 : AutoMiner.north-1;
                        north.setMessage(Text.of(String.valueOf(AutoMiner.north)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void appendNarrations(NarrationMessageBuilder builder) {

                    }

                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.north = AutoMiner.north == 8 ? 8 : AutoMiner.north+1;
                        north.setMessage(Text.of(String.valueOf(AutoMiner.north)));
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
                final TextFieldWidget south = addDrawableChild(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(AutoMiner.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new ClickableWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void appendNarrations(NarrationMessageBuilder builder) {

                    }

                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.south = AutoMiner.south == 0 ? 0 : AutoMiner.south-1;
                        south.setMessage(Text.of(String.valueOf(AutoMiner.south)));
                    }
                });
                addDrawableChild(new ClickableWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    public void appendNarrations(NarrationMessageBuilder builder) { }

                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        AutoMiner.south = AutoMiner.south == 8 ? 8 : AutoMiner.south+1;
                        south.setMessage(Text.of(String.valueOf(AutoMiner.south)));
                    }
                });
                y+=25;

                // select blocks we can mine
                addDrawableChild(new ClickableWidget(x, y, 210, 20, Text.of("Mineable blocks")) {
                    @Override
                    public void appendNarrations(NarrationMessageBuilder builder) {

                    }

                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        MinecraftClient.getInstance().setScreen(new BlocksSelectionScreen());
                    }
                });
                y += 25;

                //print selected blocks
                int start_y = y;

                List<Item> sorted = mineable.stream().sorted(Comparator.comparing(bi -> bi.getName().asString())).collect(Collectors.toList());
                for (Item it : sorted) {
                    addDrawableChild(new ClickableWidget(x, y, 100, 20, it.getName()) {
                        @Override
                        public void appendNarrations(NarrationMessageBuilder builder) {

                        }

                        @Override
                        public void onClick(double mouseX, double mouseY) {
                            MinecraftClient.getInstance().setScreen(getConfigScreen(new MainScreen(Text.of("FabricHax")), AutoMiner.class.getSimpleName()));
                            mineable.remove(it);
                        }
                    });
                    y += 25;
                    if (y > this.height-20) {
                        x += 110;
                        y = start_y;
                    }
                }
            }
        };
    }

    class BlocksSelectionScreen extends CreativeInventoryScreen {
        GameMode a, b;

        public BlocksSelectionScreen() {
            super(new PlayerEntity(Settings.world, Settings.player.getBlockPos(), 0, new GameProfile(UUID.randomUUID(), "createScreen")) {
                @Override
                public boolean isSpectator() {
                    return false;
                }

                @Override
                public boolean isCreative() {
                    return true;
                }
            });
        }

        @Override
        public void onClose() {
            client.interactionManager.setGameModes(a, b);
            super.onClose();
            MinecraftClient.getInstance().setScreen(getConfigScreen(new MainScreen(Text.of("FabricHax")), AutoMiner.class.getSimpleName()));
        }

        @Override
        protected void init() {
            if (a == null) a = client.interactionManager.getCurrentGameMode();
            if (b == null) b = client.interactionManager.getPreviousGameMode();
            client.interactionManager.setGameModes(GameMode.CREATIVE, GameMode.CREATIVE);
            super.init();
        }

        @Override
        protected void onMouseClick(@Nullable Slot slot, int slotId, int button, SlotActionType actionType) {
            if (actionType == SlotActionType.PICKUP) {
                if (slot != null) mineable.add(slot.getStack().getItem());
                onClose();
            }
        }
    }
}
