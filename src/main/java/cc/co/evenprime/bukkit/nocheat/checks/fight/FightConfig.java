package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class FightConfig implements ConfigItem {
    public final boolean directionCheck;
    public final double directionPrecision;
    public final ActionList directionActions;
    public final long directionPenaltyTime;
    public final boolean noswingCheck;
    public final ActionList noswingActions;
    public final boolean reachCheck;
    public final double reachLimit;
    public final long reachPenaltyTime;
    public final ActionList reachActions;
    public final int speedAttackLimit;
    public final ActionList speedActions;
    public final boolean speedCheck;
    public final boolean velocityCheck;
    public final double velocityThreshold;
    public final ActionList velocityActions;
    public final boolean angleCheck;
    public final double angleLimit;
    public final ActionList angleActions;
    public final boolean criticalCheck;
    public final double criticalFallDistance;
    public final double criticalVelocity;
    public final ActionList criticalActions;
    public final boolean godModeCheck;
    public final ActionList godModeActions;
    public final boolean selfHitCheck;
    public final ActionList selfHitActions;
    public final boolean fastHealCheck;
    public final long fastHealInterval;
    public final long fastHealBuffer;
    public final ActionList fastHealActions;

    public FightConfig(NoCheatConfiguration data) {
        this.directionCheck = data.getBoolean("checks.fight.direction.active");
        this.directionPrecision = data.getInt("checks.fight.direction.precision") / 100.0;
        this.directionPenaltyTime = data.getInt("checks.fight.direction.penaltytime");
        this.directionActions = data.getActionList("checks.fight.direction.actions", "nocheat.checks.fight.direction");
        this.noswingCheck = data.getBoolean("checks.fight.noswing.active");
        this.noswingActions = data.getActionList("checks.fight.noswing.actions", "nocheat.checks.fight.noswing");
        this.reachCheck = data.getBoolean("checks.fight.reach.active");
        this.reachLimit = data.getInt("checks.fight.reach.distance") / 100.0;
        this.reachPenaltyTime = data.getInt("checks.fight.reach.penaltytime");
        this.reachActions = data.getActionList("checks.fight.reach.actions", "nocheat.checks.fight.reach");
        this.speedCheck = data.getBoolean("checks.fight.speed.active");
        this.speedActions = data.getActionList("checks.fight.speed.actions", "nocheat.checks.fight.speed");
        this.speedAttackLimit = data.getInt("checks.fight.speed.attacklimit");
        this.velocityCheck = data.getBoolean("checks.fight.velocity.active");
        this.velocityThreshold = data.getInt("checks.fight.velocity.threshold") / 100.0;
        this.velocityActions = data.getActionList("checks.fight.velocity.actions", "nocheat.checks.fight.velocity");
        this.angleCheck = data.getBoolean("checks.fight.angle.active");
        this.angleLimit = data.getInt("checks.fight.angle.limit");
        this.angleActions = data.getActionList("checks.fight.angle.actions", "nocheat.checks.fight.angle");
        this.criticalCheck = data.getBoolean("checks.fight.critical.active");
        this.criticalFallDistance = data.getDouble("checks.fight.critical.falldistance");
        this.criticalVelocity = data.getDouble("checks.fight.critical.velocity");
        this.criticalActions = data.getActionList("checks.fight.critical.actions", "nocheat.checks.fight.critical");
        this.godModeCheck = data.getBoolean("checks.fight.godmode.active");
        this.godModeActions = data.getActionList("checks.fight.godmode.actions", "nocheat.checks.fight.godmode");
        this.selfHitCheck = data.getBoolean("checks.fight.selfhit.active");
        this.selfHitActions = data.getActionList("checks.fight.selfhit.actions", "nocheat.checks.fight.selfhit");
        this.fastHealCheck = data.getBoolean("checks.fight.fastheal.active");
        this.fastHealInterval = data.getLong("checks.fight.fastheal.interval", 4000L);
        this.fastHealBuffer = data.getLong("checks.fight.fastheal.buffer", 1000L);
        this.fastHealActions = data.getActionList("checks.fight.fastheal.actions", "nocheat.checks.fight.fastheal");
    }
}
