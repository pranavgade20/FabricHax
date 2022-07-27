package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Hax;
import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.network.message.MessageSignature;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.security.SignatureException;
import java.time.Instant;
import java.util.Map;

@Mixin(ClientPlayerEntity.class)
public class ChatManager {
    @Inject(at = @At("HEAD"), method = "sendChatMessage(Ljava/lang/String;)V", cancellable = true)
    private void handleChatCommand(String text, CallbackInfo ci) {
        if (text.startsWith("~")) {
            if (text.length() == 1) {
                //print help message
                Settings.player.sendMessage(Text.of(getHelpMessage()), false);
                ci.cancel();
                return;
            }
            if (text.startsWith("~ ~")) {
                String text_substring = text.substring(2);
                var message = Text.literal(text_substring);
                ChatMessageSigner chatMessageSigner = ChatMessageSigner.create(Settings.player.getUuid());
                MessageSignature messageSignature = MessageSignature.none();
                chatMessageSigner.sign(Settings.client.getProfileKeys().getSigner(), message);
                Utils.sendPacket(new ChatMessageC2SPacket(text_substring, messageSignature, false));
            }
            try {
                text = text.substring(2);
                String command = text.split("[ :]")[0];
                switch (command.toLowerCase()) {
                    case "toggle" -> {
                        String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getModuleName().toLowerCase().replace("hax", "").equals(module)) {
                                v.toggle();
                            }
                        });
                        break;
                    }
                    case "enable" -> {
                        String parameter = text.split("[ ]")[1]; // to use inside lambda

                        String module = parameter.toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getModuleName().toLowerCase().replace("hax", "").equals(module)) {
                                if (v.isEnabled()) {
                                    Settings.player.sendMessage(Text.of(parameter + " is already enabled."), false);
                                } else {
                                    v.toggle();
                                }
                            }
                        });
                        break;
                    }
                    case "disable" -> {
                        String parameter = text.split("[ ]")[1]; // to use inside lambda

                        String module = parameter.toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getModuleName().toLowerCase().replace("hax", "").equals(module)) {
                                if (!v.isEnabled()) {
                                    Settings.player.sendMessage(Text.of(parameter + " is already disabled."), false);
                                } else {
                                    v.toggle();
                                }
                            }
                        });
                        break;
                    }
                    case "hotkey" -> {
                        char parameter = text.split("[ :]")[1].toUpperCase().charAt(0);
                        if (Settings.toggles.containsKey((int) parameter)) {
                            Settings.player.sendMessage(Text.of("Key already in use by " + Settings.toggles.get((int) parameter).getModuleName()), false);
                        } else {
                            String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");
                            for (Map.Entry<Integer, Hax<?>> k : Settings.toggles.entrySet()) {
                                if (k.getValue().getModuleName().toLowerCase().replace("hax", "").equals(module)) {
                                    Settings.toggles.remove(k.getKey());
                                    Settings.toggles.put((int) parameter, k.getValue());
                                    break;
                                }
                            }
                            Settings.player.sendMessage(Text.of("Changed hotkey."), false);
                        }
                        break;
                    }
                    case "list" -> {
                        Settings.player.sendMessage(Text.of("Available modules are: "), false);
                        Settings.toggles.forEach((k, v) -> Settings.player.sendMessage(Text.of(v.getModuleName()), false));
                    }
                    case "help" -> {
                        String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getModuleName().toLowerCase().replace("hax", "").equals(module))
                                Settings.player.sendMessage(Text.of(v.getHelpMessage()), false);
                        });
                        break;
                    }
                    case "hotkeys" -> Settings.toggles.forEach((key, module) -> {
                        if (key > 0) {
                            Settings.player.sendMessage(Text.of((char) key.intValue() + " - " + module.getModuleName()), false);
                        }
                    });
                    case "config" -> {
                        String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");
                        String params = text.substring(text.indexOf(" ", text.indexOf(" "))).trim();
                        Settings.toggles.values().forEach(v -> {
                            if (v.getModuleName().toLowerCase().replace("hax", "").equals(module)) {
                                v.config(params);
                            }
                        });
                        break;
                    }
                }
                ci.cancel();
            } catch (Exception e) {
                Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help) for more information."), false);
            }
        }
    }

    private static String getHelpMessage() {
        return """
                FabricHax - a client side mod to make your life easier!
                Usage: ~ <command> <module>
                       (to execute a command)
                   or  ~ ~<message>
                       (to send a <message> starting with ~)
                where command includes one of
                 toggle - toggle the module
                 enable - enable the module
                 disable - disable the module
                 help - show help about the module
                 hotkey:<key> - change hotkey to toggle the module
                 list - list all modules
                 config - configure the module, refer to ~ help <module> for more information""";
    }
}
