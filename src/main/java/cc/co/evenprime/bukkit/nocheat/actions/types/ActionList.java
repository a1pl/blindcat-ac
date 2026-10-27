package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.actions.Action;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionList {
    public final String permissionSilent;
    private static final Action[] emptyArray = new Action[0];
    private final Map<Integer, Action[]> actions = new HashMap<>();
    private final List<Integer> tresholds = new ArrayList<>();

    public ActionList(String permission) {
        this.permissionSilent = permission + ".silent";
    }

    public void setActions(Integer treshold, Action[] actions) {
        if (!this.tresholds.contains(treshold)) {
            this.tresholds.add(treshold);
            Collections.sort(this.tresholds);
        }

        this.actions.put(treshold, actions);
    }

    public Action[] getActions(double violationLevel) {
        Integer result = null;

        for (Integer treshold : this.tresholds) {
            if (treshold.intValue() <= violationLevel) {
                result = treshold;
            }
        }

        return result != null ? this.actions.get(result) : emptyArray;
    }

    public List<Integer> getTresholds() {
        return this.tresholds;
    }
}
