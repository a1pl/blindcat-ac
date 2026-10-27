package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.actions.Action;

public class DummyAction extends Action {
    private final String def;

    public DummyAction(String def) {
        super("dummyAction", 10000, 10000);
        this.def = def;
    }

    @Override
    public String toString() {
        return this.def;
    }
}
