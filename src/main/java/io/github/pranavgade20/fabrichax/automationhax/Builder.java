package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class Builder extends AutomationBase {
    public static int left = 2;
    public static int right = 2;

    public static Builder INSTANCE;
    public Builder() {
        INSTANCE = this;
    }

    public static void build(Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());

        for (int i = -left; i < right; i++) {
            switch (Settings.player.getHorizontalFacing()) {
                case NORTH -> {
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).withBlockPos(pos.west(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).withBlockPos(pos.east(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                }
                case SOUTH -> {
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).withBlockPos(pos.west(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).withBlockPos(pos.east(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                }
                case EAST -> {
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).withBlockPos(pos.north(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).withBlockPos(pos.south(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                }
                case WEST -> {
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).withBlockPos(pos.north(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).withBlockPos(pos.south(j));
                        Utils.sendPacket(seq -> new PlayerInteractBlockC2SPacket(hand, result, seq));
                    }
                }
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return """
                Builder - place large chunks of blocks quickly.
                Do not stand where the blocks will be placed for best results

                Configuration information:
                 ~ config Builder <direction> <size>
                 (to configure shape to be built)
                 where directions include 'left, right'
                 for example, use `~ config Builder left 2`
                 to set this to place 2 blocks to the left of your placed block.""";
    }

    @Override
    public HashMap<String, String> getArgs() {
        HashMap<String, String> ret = super.getArgs();
        ret.put("left", String.valueOf(left));
        ret.put("right", String.valueOf(right));
        return ret;
    }

    @Override
    public void setArgs(HashMap<String, String> args) {
        super.setArgs(args);

        left = Integer.parseInt(args.get("left"));
        right = Integer.parseInt(args.get("right"));
    }

    @Override
    public String getToolTip() {
        return "Place large chunks of blocks quickly";
    }

    @Override
    public void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            if (direction.equals("left")) left = size;
            else if (direction.equals("right")) right = size;
            else {
                Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help builder) for more information."), false);
                return;
            }
            Settings.player.sendMessage(Text.of("~ config Builder " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help builder) for more information."), false);
        }
    }

    @Override
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

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Left")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget left = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(Builder.left))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    Builder.left = Builder.left == 0 ? 0 : Builder.left - 1;
                    left.setMessage(Text.of(String.valueOf(Builder.left)));
                }, textRenderer));

                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    Builder.left = Builder.left == 8 ? 8 : Builder.left + 1;
                    left.setMessage(Text.of(String.valueOf(Builder.left)));
                }, textRenderer));
                y += 25;

                addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Right")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget right = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(Builder.right))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredTextWithShadow(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });

                addDrawableChild(new PressableTextWidget(x + 110, y, 20, 20, Text.of("-"), button -> {
                    Builder.right = Builder.right == 0 ? 0 : Builder.right - 1;
                    right.setMessage(Text.of(String.valueOf(Builder.right)));

                }, textRenderer));
                addDrawableChild(new PressableTextWidget(x + 110 + 25 + 55, y, 20, 20, Text.of("+"), button -> {
                    Builder.right = Builder.right == 8 ? 8 : Builder.right + 1;
                    right.setMessage(Text.of(String.valueOf(Builder.right)));

                }, textRenderer));
            }
        };
    }
}
