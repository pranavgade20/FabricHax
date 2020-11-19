package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

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
                case NORTH:
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).withBlockPos(pos.west(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).withBlockPos(pos.east(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case SOUTH:
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).withBlockPos(pos.west(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).withBlockPos(pos.east(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case EAST:
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).withBlockPos(pos.north(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).withBlockPos(pos.south(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case WEST:
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).withBlockPos(pos.north(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).withBlockPos(pos.south(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
            }
        }
    }

    @Override
    public String getHelpMessage() {
        return "Builder - place large chunks of blocks quickly.\n" +
                "Do not stand where the blocks will be placed for best results\n" +
                "\nConfiguration information:\n" +
                " ~ config Builder <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'left, right'\n" +
                " for example, use `~ config Builder left 2`\n" +
                " to set this to place 2 blocks to the left of your placed block.";
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

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Left")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget left = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(Builder.left))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Builder.left = Builder.left == 0 ? 0 : Builder.left-1;
                        left.setMessage(Text.of(String.valueOf(Builder.left)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Builder.left = Builder.left == 8 ? 8 : Builder.left+1;
                        left.setMessage(Text.of(String.valueOf(Builder.left)));
                    }
                });
                y+=25;

                addButton(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of("Right")) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                final TextFieldWidget right = addButton(new TextFieldWidget(textRenderer, x+110+25, y, 50, 20, Text.of(String.valueOf(Builder.right))) {
                    @Override
                    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });
                addButton(new AbstractButtonWidget(x+110, y, 20, 20, Text.of("-")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Builder.right = Builder.right == 0 ? 0 : Builder.right-1;
                        right.setMessage(Text.of(String.valueOf(Builder.right)));
                    }
                });
                addButton(new AbstractButtonWidget(x+110+25+55, y, 20, 20, Text.of("+")) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        Builder.right = Builder.right == 8 ? 8 : Builder.right+1;
                        right.setMessage(Text.of(String.valueOf(Builder.right)));
                    }
                });
            }
        };
    }
}
