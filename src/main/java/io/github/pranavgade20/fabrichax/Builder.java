package io.github.pranavgade20.fabrichax;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Builder {
    public static boolean enabled = false;

    public static int left = 2;
    public static int right = 2;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Builder"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Builder"), Settings.player.getUuid());
        }
    }

    public static void build(Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());

        for (int i = -left; i < right; i++) {
            switch (Settings.player.getHorizontalFacing()) {
                case NORTH:
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).method_29328(pos.west(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).method_29328(pos.east(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case SOUTH:
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.EAST).method_29328(pos.west(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.WEST).method_29328(pos.east(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case EAST:
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).method_29328(pos.north(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).method_29328(pos.south(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
                case WEST:
                    for (int j = 1; j <= right; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.SOUTH).method_29328(pos.north(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    for (int j = 1; j <= left; j++) {
                        BlockHitResult result = hitResult.withSide(Direction.NORTH).method_29328(pos.south(j));
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, result));
                    }
                    break;
            }
        }
    }

    public static String getHelpMessage() {
        return "Builder - place large chunks of blocks quickly.\n" +
                "Do not stand where the blocks will be placed for best results\n" +
                "\nConfiguration information:\n" +
                " ~ config Builder <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'left, right'\n" +
                " for example, use `config Builder left 2`\n" +
                " to set this to place 2 blocks to the left of your placed block.";
    }

    public static void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            if (direction.equals("left")) left = size;
            else if (direction.equals("right")) right = size;
            else {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help builder) for more information."), Settings.player.getUuid());
                return;
            }
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("~ config Builder " + params), Settings.player.getUuid());
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help builder) for more information."), Settings.player.getUuid());
        }
    }
}
