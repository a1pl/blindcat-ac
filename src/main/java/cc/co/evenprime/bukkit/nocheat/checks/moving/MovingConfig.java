package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class MovingConfig implements ConfigItem {
    public final boolean runflyCheck;
    public final boolean identifyCreativeMode;
    public final double walkingSpeedLimit;
    public final double sprintingSpeedLimit;
    public final double jumpheight;
    public final double swimmingSpeedLimit;
    public final boolean sneakingCheck;
    public final double sneakingSpeedLimit;
    public final ActionList actions;
    public final boolean allowFlying;
    public final double flyingSpeedLimitVertical;
    public final double flyingSpeedLimitHorizontal;
    public final ActionList flyingActions;
    public final boolean nofallCheck;
    public final boolean nofallaggressive;
    public final float nofallMultiplier;
    public final ActionList nofallActions;
    public final boolean morePacketsCheck;
    public final ActionList morePacketsActions;
    public final boolean waterwalkCheck;
    public final int waterwalkTicks;
    public final ActionList waterwalkActions;
    public final boolean passableCheck;
    public final ActionList passableActions;
    public final int flyingHeightLimit;

    public MovingConfig(NoCheatConfiguration data) {
        this.identifyCreativeMode = data.getBoolean("checks.moving.runfly.flying.allowflyingincreative");
        this.runflyCheck = data.getBoolean("checks.moving.runfly.active");
        int walkspeed = data.getInt("checks.moving.runfly.walkspeed", 100);
        int sprintspeed = data.getInt("checks.moving.runfly.sprintspeed", 100);
        int swimspeed = data.getInt("checks.moving.runfly.swimspeed", 100);
        int sneakspeed = data.getInt("checks.moving.runfly.sneakspeed", 100);
        this.walkingSpeedLimit = 0.22 * walkspeed / 100.0;
        this.sprintingSpeedLimit = 0.35 * sprintspeed / 100.0;
        this.swimmingSpeedLimit = 0.18 * swimspeed / 100.0;
        this.sneakingSpeedLimit = 0.14 * sneakspeed / 100.0;
        this.jumpheight = 1.35;
        this.sneakingCheck = !data.getBoolean("checks.moving.runfly.allowfastsneaking");
        this.actions = data.getActionList("checks.moving.runfly.actions", "nocheat.checks.moving.runfly");
        this.allowFlying = data.getBoolean("checks.moving.runfly.flying.allowflyingalways");
        this.flyingSpeedLimitVertical = data.getInt("checks.moving.runfly.flying.flyingspeedlimitvertical") / 100.0;
        this.flyingSpeedLimitHorizontal = data.getInt("checks.moving.runfly.flying.flyingspeedlimithorizontal") / 100.0;
        this.flyingHeightLimit = data.getInt("checks.moving.runfly.flying.flyingheightlimit");
        this.flyingActions = data.getActionList("checks.moving.runfly.flying.actions", "nocheat.checks.moving.flying");
        this.nofallCheck = data.getBoolean("checks.moving.runfly.checknofall");
        this.nofallMultiplier = 2.0F;
        this.nofallaggressive = data.getBoolean("checks.moving.runfly.nofallaggressivemode");
        this.nofallActions = data.getActionList("checks.moving.runfly.nofallactions", "nocheat.checks.moving.nofall");
        this.morePacketsCheck = data.getBoolean("checks.moving.morepackets.active");
        this.morePacketsActions = data.getActionList("checks.moving.morepackets.actions", "nocheat.checks.moving.morepackets");
        this.waterwalkCheck = data.getBoolean("checks.moving.waterwalk.active");
        this.waterwalkTicks = data.getInt("checks.moving.waterwalk.ticks", 30);
        this.waterwalkActions = data.getActionList("checks.moving.waterwalk.actions", "nocheat.checks.moving.waterwalk");
        this.passableCheck = data.getBoolean("checks.moving.passable.active");
        this.passableActions = data.getActionList("checks.moving.passable.actions", "nocheat.checks.moving.passable");
    }
}
