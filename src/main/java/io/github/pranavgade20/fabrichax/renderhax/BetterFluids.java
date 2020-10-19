package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Base;

public class BetterFluids extends RenderBase {
    public static BetterFluids INSTANCE;
    public BetterFluids() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }

    @Override
    public String getHelpMessage() {
        return "BetterFluids - Improves fluid rendering to render fluid sides as well.\n" +
                "Use this with xray to avoid walking into lava";
    }

    @Override
    public String getToolTip() {
        return "Render fluid sides as well";
    }
}
