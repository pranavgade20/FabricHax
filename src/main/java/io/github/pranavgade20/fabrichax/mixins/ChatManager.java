package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(ClientPlayerEntity.class)
public class ChatManager {
    @Inject(at = @At("HEAD"), method = "sendChatMessage(Ljava/lang/String)V", cancellable = true)
    private void handleChatCommand(String text, CallbackInfo ci) {
        if (text.startsWith("~")) {
            if (text.length() == 1) {
                //print help message
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of(getHelpMessage()), Settings.player.getUuid());
            }
            if (text.startsWith("~ ~")) {
                ClientSidePacketRegistry.INSTANCE.sendToServer(new ChatMessageC2SPacket(text.substring(2)));
            }
            text = text.substring(2);
            String command = text.split("[ :]")[0];
            if (command.toLowerCase().equals("toggle")) {
                try {
                    String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");
                    Settings.toggles.values().forEach(v -> {
                        if (v.getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                            try {
                                v.getMethod("toggle").invoke(null);
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help) for more information."), Settings.player.getUuid());
                }
            }
            ci.cancel();
        }
    }

    private static String getHelpMessage() {
        return "FabricHax - a client side mod to make your life easier!\n" +
                "Usage: ~ <command> <module>\n" +
                "       (to execute a command)\n" +
                "   or  ~ ~<message>\n" +
                "       (to send a <message> starting with ~)\n" +
                "where command includes one of\n" +
                " toggle        toggle the module\n" +
                " enable        enable the module\n" +
                " disable       disable the module\n" +
                " help          show help about the module\n" +
                " hotkey:<key>  change hotkey to toggle the module\n" +
                " list          list all modules";
    }
}
