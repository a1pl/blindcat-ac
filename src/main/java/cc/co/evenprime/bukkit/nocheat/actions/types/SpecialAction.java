package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.actions.Action;

public class SpecialAction extends Action {
    public SpecialAction() {
        super("cancel", 0, 0);
    }

    @Override
    public String toString() {
        return "cancel";
    }
}
