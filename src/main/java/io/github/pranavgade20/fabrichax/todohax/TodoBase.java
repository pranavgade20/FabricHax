package io.github.pranavgade20.fabrichax.todohax;

import io.github.pranavgade20.fabrichax.Base;

public class TodoBase extends Base {
    public static TodoBase INSTANCE;
    static {
        INSTANCE = new TodoBase();
        INSTANCE.enabled = false;
    }

    @Override
    public String getToolTip() {
        return "TODO - I am trying to polish these before release.";
    }
}
