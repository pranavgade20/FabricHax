package io.github.pranavgade20.fabrichax.gui;

import io.github.pranavgade20.fabrichax.Base;
import io.github.pranavgade20.fabrichax.Hax;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Comparator;

public class CategoryScreen extends Screen {
    static Text status = Text.of("FabricHax");
    private Hax<? extends Base> module;

    public CategoryScreen(Hax<? extends Base> module) {
        super(Text.of(module.getModuleName()));
        status = Text.of(module.getToolTip());
        this.module = module;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, status, this.width / 2, 10, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;
        Hax<? extends Base>[] sortedEntries = Settings.toggles.values().toArray(new Hax<?>[0]);
        Arrays.sort(sortedEntries, Comparator.comparing(Hax::getModuleName));
        for (Hax<?> entry : sortedEntries) {
            try {
                if (module.getModuleClass().isInstance(entry.getModule())){
                    addButton(new AbstractButtonWidget(x, y, 100, 20, Text.of(entry.getModuleName())) {
                        @Override
                        public void onRelease(double mouseX, double mouseY) {
                            MinecraftClient.getInstance().openScreen(entry.getModule().getConfigScreen(CategoryScreen.this, entry.getModuleName()));
                        }
                    });
                    y += 25;
                    if (y > this.height - 20) {
                        x += 110;
                        y = 30;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = Text.of("Error initializing modules");
            }
        }
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(MainScreen.INSTANCE);
    }
}
