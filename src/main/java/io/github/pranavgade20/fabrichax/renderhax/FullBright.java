package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Base;

public class FullBright extends Base {
    public static FullBright INSTANCE;
    public FullBright() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "FullBright - Improves visibility by making all blocks 100% bright\n" +
                "Use this with xray to find ores.";
    }

    @Override
    public String getToolTip() {
        return "Make all blocks 100% bright";
    }
}
