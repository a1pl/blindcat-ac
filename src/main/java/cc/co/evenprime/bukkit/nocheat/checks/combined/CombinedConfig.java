package cc.co.evenprime.bukkit.nocheat.checks.combined;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class CombinedConfig implements ConfigItem {
    public final boolean improbableCheck;
    public final double improbableLevel;
    public final ActionList improbableActions;

    public CombinedConfig(NoCheatConfiguration data) {
        this.improbableCheck = data.getBoolean("checks.combined.improbable.active");
        this.improbableLevel = data.getInt("checks.combined.improbable.level");
        this.improbableActions = data.getActionList("checks.combined.improbable.actions", "nocheat.checks.combined.improbable");
    }
}
