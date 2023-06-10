package io.github.pranavgade20.fabrichax.automationhax;

import com.mojang.authlib.GameProfile;
import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.gui.MainScreen;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AutoMiner extends AutomationBase {
    public static int up = 3;
    public static int down = 0;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static int count = 0;

    public static HashSet<Item> mineable = new HashSet<>();

    public static AutoMiner INSTANCE;
    public AutoMiner() {
        INSTANCE = this;
    }

    public String getHelpMessage() {
        return """
                AutoMiner - automatically mine blocks around you

                Configuration information:
                 ~ config AutoMiner <direction> <size>
                 (to configure shape to be built)
                 where directions include 'up, down, north, south, east, west'
                 for example, use `~ config AutoMiner left 2`
                 to set this to plant 2 blocks to your left.""";
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
        ret.put("mineable", mineable.stream()
                .map(i -> i.getName().getString())
                .reduce((a, b) -> a + ";" + b)
                .orElse("NOTHING"));
        System.out.println(ret.get("mineable"));
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
        HashSet<String> set = new HashSet<>(Arrays.stream(args.get("mineable").split(";")).toList());
        for (Block block : Registries.BLOCK) {
            if (set.contains(block.asItem().getName().getString())) mineable.add(block.asItem());
        }
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
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AutoMiner) for more information."), false);
                    return;
                }
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
                addDrawableChild(new PressableTextWidget(x + 110, y, 100, 20, Text.of(String.valueOf(enabled)), button -> button.setMessage(Text.of(String.valueOf(enabled))), textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Up")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget up = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.up))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });

                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AutoMiner.up = AutoMiner.up == 0 ? 0 : AutoMiner.up - 1;
                    up.setMessage(Text.of(String.valueOf(AutoMiner.up)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AutoMiner.up = AutoMiner.up == 8 ? 8 : AutoMiner.up + 1;
                    up.setMessage(Text.of(String.valueOf(AutoMiner.up)));
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Down")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget down = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.down))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("West")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget west = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.west))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("East")) {
                    public void appendClickableNarrations(NarrationMessageBuilder builder) {
                    }

                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget east = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.east))) {
                    public void appendClickableNarrations(NarrationMessageBuilder builder) {
                    }

                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("North")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget north = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.north))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AutoMiner.north = AutoMiner.north == 0 ? 0 : AutoMiner.north - 1;
                    north.setMessage(Text.of(String.valueOf(AutoMiner.north)));
                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    AutoMiner.north = AutoMiner.north == 8 ? 8 : AutoMiner.north + 1;
                    north.setMessage(Text.of(String.valueOf(AutoMiner.north)));
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("South")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget south = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(AutoMiner.south))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    AutoMiner.south = AutoMiner.south == 0 ? 0 : AutoMiner.south - 1;
                    south.setMessage(Text.of(String.valueOf(AutoMiner.south)));
                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                }, textRenderer));
                y += 25;

                // select blocks we can mine
                addDrawableChild(new PressableTextWidget(x, y, 210, 20, Text.of("Mineable blocks"), button -> {
                    MinecraftClient.getInstance().setScreen(new BlocksSelectionScreen());
                }, textRenderer));
                y += 25;

                //print selected blocks
                int start_y = y;

                List<Item> sorted = mineable.stream().sorted(Comparator.comparing(bi -> bi.getName().getString())).toList();
                for (Item it : sorted) {
                    addDrawableChild(new PressableTextWidget(x, y, 100, 20, it.getName(), button -> {
                        MinecraftClient.getInstance().setScreen(getConfigScreen(new MainScreen(Text.of("FabricHax")), AutoMiner.class.getSimpleName()));
                        mineable.remove(it);
                    }, textRenderer));
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
            }, Settings.world.getEnabledFeatures(), false);
        }

        @Override
        public void close() {
            client.interactionManager.setGameModes(a, b);
            super.close();
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
                close();
            }
        }
    }
}
