package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class BlockPlaceConfig implements ConfigItem {
    public final boolean reachCheck;
    public final double reachDistance;
    public final ActionList reachActions;
    public final boolean directionCheck;
    public final ActionList directionActions;
    public final long directionPenaltyTime;
    public final double directionPrecision;
    public final boolean fastPlaceCheck;
    public final long fastPlaceDelay;
    public final ActionList fastPlaceActions;

    public BlockPlaceConfig(NoCheatConfiguration data) {
        this.reachCheck = data.getBoolean("checks.blockplace.reach.active");
        this.reachDistance = 5.35;
        this.reachActions = data.getActionList("checks.blockplace.reach.actions", "nocheat.checks.blockplace.reach");
        this.directionCheck = data.getBoolean("checks.blockplace.direction.active");
        this.directionPenaltyTime = data.getInt("checks.blockplace.direction.penaltytime");
        this.directionPrecision = data.getInt("checks.blockplace.direction.precision") / 100.0;
        this.directionActions = data.getActionList("checks.blockplace.direction.actions", "nocheat.checks.blockplace.direction");
        this.fastPlaceCheck = data.getBoolean("checks.blockplace.fastplace.active");
        this.fastPlaceDelay = data.getInt("checks.blockplace.fastplace.delay");
        this.fastPlaceActions = data.getActionList("checks.blockplace.fastplace.actions", "nocheat.checks.blockplace.fastplace");
    }
}
