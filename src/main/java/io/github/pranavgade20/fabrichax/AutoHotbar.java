package io.github.pranavgade20.fabrichax;

public class AutoHotbar extends Base{
    public static AutoHotbar INSTANCE;
    public AutoHotbar() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }

    @Override
    public String getHelpMessage() {
        return "AutoHotbar - the ultimate hotbar management solution.\n" +
                "This automatically scrolls your main hand so you are holding the right tool for the job. " +
                "For example, switching to pickaxe when mining stone and switching to sword when attacking a zombie. " +
                "p.s.: for this to work, the tools need to be on your hotbar.";
    }

    @Override
    public String getToolTip() {
        return "The ultimate hotbar management solution";
    }
}
