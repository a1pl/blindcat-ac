package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class InventoryConfig implements ConfigItem {
    public final boolean dropCheck;
    public final long dropTimeFrame;
    public final int dropLimit;
    public final ActionList dropActions;
    public final boolean bowCheck;
    public final ActionList bowActions;
    public final boolean eatCheck;
    public final ActionList eatActions;

    public InventoryConfig(NoCheatConfiguration data) {
        this.dropCheck = data.getBoolean("checks.inventory.drop.active");
        this.dropTimeFrame = data.getInt("checks.inventory.drop.time") * 1000;
        this.dropLimit = data.getInt("checks.inventory.drop.limit");
        this.dropActions = data.getActionList("checks.inventory.drop.actions", "nocheat.checks.inventory.drop");
        this.bowCheck = data.getBoolean("checks.inventory.instantbow.active");
        this.bowActions = data.getActionList("checks.inventory.instantbow.actions", "nocheat.checks.inventory.instantbow");
        this.eatCheck = data.getBoolean("checks.inventory.instanteat.active");
        this.eatActions = data.getActionList("checks.inventory.instanteat.actions", "nocheat.checks.inventory.instanteat");
    }
}
