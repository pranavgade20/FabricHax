package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.Settings;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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
                " for example, use `config Builder left 2`\n" +
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
}
