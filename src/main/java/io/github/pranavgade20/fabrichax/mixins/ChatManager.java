package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
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
                ClientSidePacketRegistry.INSTANCE.sendToServer(new ChatMessageC2SPacket(text.substring(2)));
            }
            try {
                text = text.substring(2);
                String command = text.split("[ :]")[0];
                switch (command.toLowerCase()) {
                    case "toggle": {
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
                        break;
                    }
                    case "enable": {
                        String parameter = text.split("[ ]")[1]; // to use inside lambda

                        String module = parameter.toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                                try {
                                    if (v.getDeclaredField("enabled").getBoolean(null)) {
                                        Settings.player.sendMessage(Text.of(parameter + " is already enabled."), false);
                                    } else {
                                        try {
                                            v.getMethod("toggle").invoke(null);
                                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    }
                    case "disable": {
                        String parameter = text.split("[ ]")[1]; // to use inside lambda

                        String module = parameter.toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                                try {
                                    if (!v.getDeclaredField("enabled").getBoolean(null)) {
                                        Settings.player.sendMessage(Text.of(parameter + " is already disabled."), false);
                                    } else {
                                        try {
                                            v.getMethod("toggle").invoke(null);
                                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    }
                    case "hotkey": {
                        char parameter = text.split("[ :]")[1].toUpperCase().charAt(0);
                        if (Settings.toggles.containsKey((int) parameter)) {
                            Settings.player.sendMessage(Text.of("Key already in use by " + Settings.toggles.get((int) parameter).getSimpleName()), false);
                        } else {
                            String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");
                            for (Map.Entry<Integer, Class> k : Settings.toggles.entrySet()) {
                                if (k.getValue().getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                                    Settings.toggles.remove(k.getKey());
                                    Settings.toggles.put((int) parameter, k.getValue());
                                    break;
                                }
                            }
                            Settings.player.sendMessage(Text.of("Changed hotkey."), false);
                        }
                        break;
                    }
                    case "list":
                        Settings.player.sendMessage(Text.of("Available modules are: "), false);
                        Settings.toggles.forEach((k, v) -> Settings.player.sendMessage(Text.of(v.getSimpleName()), false));
                        break;
                    case "help": {
                        String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");

                        Settings.toggles.values().forEach(v -> {
                            if (v.getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                                try {
                                    Settings.player.sendMessage(Text.of((String) v.getMethod("getHelpMessage").invoke(null)), false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    }
                    case "hotkeys":
                        Settings.toggles.forEach((key, module) -> {
                            if (key > 0) {
                                Settings.player.sendMessage(Text.of((char) key.intValue() + " - " + module.getSimpleName()), false);
                            }
                        });
                        break;
                    case "config": {
                        String module = text.split("[ ]")[1].toLowerCase().replace("hax", "");
                        String params = text.substring(text.indexOf(" ", text.indexOf(" "))).trim();
                        Settings.toggles.values().forEach(v -> {
                            if (v.getSimpleName().toLowerCase().replace("hax", "").equals(module)) {
                                try {
                                    v.getMethod("config", " ".getClass()).invoke(null, params);
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
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
        return "FabricHax - a client side mod to make your life easier!\n" +
                "Usage: ~ <command> <module>\n" +
                "       (to execute a command)\n" +
                "   or  ~ ~<message>\n" +
                "       (to send a <message> starting with ~)\n" +
                "where command includes one of\n" +
                " toggle - toggle the module\n" +
                " enable - enable the module\n" +
                " disable - disable the module\n" +
                " help - show help about the module\n" +
                " hotkey:<key> - change hotkey to toggle the module\n" +
                " list - list all modules\n" +
                " config - configure the module, refer to ~ help <module> for more information";
    }
}
