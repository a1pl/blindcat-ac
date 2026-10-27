package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class BlockBreakConfig implements ConfigItem {
    public final boolean reachCheck;
    public final double reachDistance;
    public final ActionList reachActions;
    public final boolean directionCheck;
    public final ActionList directionActions;
    public final double directionPrecision;
    public final long directionPenaltyTime;
    public final boolean noswingCheck;
    public final ActionList noswingActions;
    public final boolean fastBreakCheck;
    public final long fastBreakDelay;
    public final ActionList fastBreakActions;

    public BlockBreakConfig(NoCheatConfiguration data) {
        this.reachCheck = data.getBoolean("checks.blockbreak.reach.active");
        this.reachDistance = 5.35;
        this.reachActions = data.getActionList("checks.blockbreak.reach.actions", "nocheat.checks.blockbreak.reach");
        this.directionCheck = data.getBoolean("checks.blockbreak.direction.active");
        this.directionPrecision = data.getInt("checks.blockbreak.direction.precision") / 100.0;
        this.directionPenaltyTime = data.getInt("checks.blockbreak.direction.penaltytime");
        this.directionActions = data.getActionList("checks.blockbreak.direction.actions", "nocheat.checks.blockbreak.direction");
        this.noswingCheck = data.getBoolean("checks.blockbreak.noswing.active");
        this.noswingActions = data.getActionList("checks.blockbreak.noswing.actions", "nocheat.checks.blockbreak.noswing");
        this.fastBreakCheck = data.getBoolean("checks.blockbreak.fastbreak.active");
        this.fastBreakDelay = data.getInt("checks.blockbreak.fastbreak.delay");
        this.fastBreakActions = data.getActionList("checks.blockbreak.fastbreak.actions", "nocheat.checks.blockbreak.fastbreak");
    }
}
