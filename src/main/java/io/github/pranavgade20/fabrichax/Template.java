package io.github.pranavgade20.fabrichax;

public class Template extends Base{
    public static Template INSTANCE;
    public Template() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }
// Override getHelpMessage() and getToolTip()
    // Override config() and toggle() if you wanna
}
