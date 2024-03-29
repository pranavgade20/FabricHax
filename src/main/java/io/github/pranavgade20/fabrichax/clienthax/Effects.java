package io.github.pranavgade20.fabrichax.clienthax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class Effects extends ClientBase {
    public static HashMap<StatusEffect, StatusEffectInstance> cache = new HashMap<>();
    public static HashMap<String, Integer> effects = new HashMap<>();
    static {
        for (String effect : new String[]{"speed", "slowness", "jump_boost", "nausea", "night_vision", "blindness", "levitation", "slow_falling", "conduit_power", "dolphins_grace"})
            effects.put(effect, -1);
    }

    public static Effects INSTANCE;
    public Effects() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }

    @Override
    public boolean toggle() {
        if (enabled) {
            cache.forEach(((statusEffect, statusEffectInstance) -> Settings.player.getActiveStatusEffects().remove(statusEffect)));
        } else {
            cache.forEach((effect, instance) -> Settings.player.getActiveStatusEffects().put(effect, instance));
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return """
                Effects - control the status effects you have.

                Configuration information:
                 ~ config Effects <effect_name> <amplifier>
                 (select amplifier to 0 to remove the effect)
                Currently supported effects are-speed, slowness, jump_boost, nausea, night_vision, blindness, levitation, slow_falling, conduit_power, dolphins_grace""";
    }

    @Override
    public void config(String params) {
        try {
            String name = params.split(" ")[1].toLowerCase();
            StatusEffect requested = null;

            for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> e : Registries.STATUS_EFFECT.getEntrySet()) {
                if (e.getValue().getName().toString().toLowerCase().contains(name)) {
                    requested = e.getValue();
                    break;
                }
            }
            if (requested == null) {
                Settings.player.sendMessage(Text.of("Invalid effect."), false);
                return;
            }

            int amplifier = params.split(" ").length > 2 ? Integer.parseInt(params.split(" ")[2].toLowerCase())-1 : 0;

            StatusEffectInstance effectInstance = new StatusEffectInstance(requested, Integer.MAX_VALUE, amplifier);
            if (amplifier == -1) {
                Settings.player.getActiveStatusEffects().remove(requested);
                cache.remove(requested);
            } else {
                Settings.player.getActiveStatusEffects().put(requested, effectInstance);
                cache.put(requested, effectInstance);
            }

            effects.put(requested.getName().toString().toLowerCase(), amplifier);
            Settings.player.sendMessage(Text.of("Gave you " + name), false);
        } catch (Exception e) {
            e.printStackTrace();
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help template) for more information."), false);
        }
    }

    @Override
    public Screen getConfigScreen(Screen parent, String name) {
        return new Screen(Text.of(name)) {
            @Override
            public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                this.renderBackground(matrices, mouseX, mouseY, delta);
                matrices.drawCenteredTextWithShadow(this.textRenderer, getTitle(), this.width / 2, 10, 16777215);
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
                    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                        int j = this.active ? 16777215 : 10526880;
                        matrices.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    }
                });

                addDrawableChild(new ButtonWidget.Builder(Text.of(String.valueOf(enabled)), (button) -> {
                    enabled = !enabled;
                    button.setMessage(Text.of(String.valueOf(enabled)));
                }).position(x + 110, y).size(100, 20).build());
                y += 25;

                for (Map.Entry<String, Integer> entry : effects.entrySet()) {
                    String effect = entry.getKey();
                    Integer strength = entry.getValue();
                    addDrawableChild(new TextFieldWidget(textRenderer, x, y, 100, 20, Text.of(effect)) { //TODO capitalise first char
                        @Override
                        public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                            int j = this.active ? 16777215 : 10526880;
                            matrices.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                        }
                    });
                    final TextFieldWidget stren = addDrawableChild(new TextFieldWidget(textRenderer, x + 110 + 25, y, 50, 20, Text.of(String.valueOf(strength + 1))) {
                        @Override
                        public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
                            int j = this.active ? 16777215 : 10526880;
                            matrices.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
                        }
                    });
                    addDrawableChild(new ButtonWidget.Builder(Text.of("-"), (button) -> {
                                StatusEffect requested = null;

                                for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> e : Registries.STATUS_EFFECT.getEntrySet()) {
                                    if (e.getValue().getName().toString().toLowerCase().contains(effect)) {
                                        requested = e.getValue();
                                        break;
                                    }
                                }
                                if (requested == null) {
                                    Settings.player.sendMessage(Text.of("Invalid effect."), false);
                                    return;
                                }

                                int amplifier = effects.get(effect) == -1 ? -1 : effects.get(effect) - 1;

                                StatusEffectInstance effectInstance = new StatusEffectInstance(requested, Integer.MAX_VALUE, amplifier);
                                if (amplifier == -1) {
                                    Settings.player.getActiveStatusEffects().remove(requested);
                                    cache.remove(requested);
                                } else {
                                    Settings.player.getActiveStatusEffects().put(requested, effectInstance);
                                    cache.put(requested, effectInstance);
                                }

                                effects.put(effect, amplifier);

                                stren.setMessage(Text.of(String.valueOf(amplifier + 1)));
                            }).position(x + 110, y).size(20, 20).build()
                    );
                    addDrawableChild(new ButtonWidget.Builder(Text.of("+"), (button) -> {
                        StatusEffect requested = null;

                        for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> e : Registries.STATUS_EFFECT.getEntrySet()) {
                            if (e.getValue().getName().toString().toLowerCase().contains(effect)) {
                                requested = e.getValue();
                                break;
                            }
                        }
                        if (requested == null) {
                            Settings.player.sendMessage(Text.of("Invalid effect."), false);
                            return;
                        }

                        int amplifier = effects.get(effect) == 255 ? 255 : effects.get(effect) + 1;

                        StatusEffectInstance effectInstance = new StatusEffectInstance(requested, Integer.MAX_VALUE, amplifier);
                        if (amplifier == -1) {
                            Settings.player.getActiveStatusEffects().remove(requested);
                            cache.remove(requested);
                        } else {
                            Settings.player.getActiveStatusEffects().put(requested, effectInstance);
                            cache.put(requested, effectInstance);
                        }

                        effects.put(effect, amplifier);

                        stren.setMessage(Text.of(String.valueOf(amplifier + 1)));
                    }).position(x + 110 + 25 + 55, y).size(20, 20).build());

                    y += 25;
                }
            }
        };
    }
}
