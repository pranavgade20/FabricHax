package io.github.pranavgade20.fabrichax.scripting;

import java.util.LinkedList;

public class ScriptBase extends io.github.pranavgade20.fabrichax.Base {
    LinkedList<String> script;
    @Override
    public String getToolTip() {
        return "Write custom scripts!!.";
    }

    public LinkedList<String> getScript() {
        return script;
    }
}
