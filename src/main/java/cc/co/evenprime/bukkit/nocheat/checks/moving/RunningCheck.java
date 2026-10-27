package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class RunningCheck extends MovingCheck {
    private static final double maxBonus = 1.0;
    private static final int jumpingLimit = 6;
    private final NoFallCheck noFallCheck;

    public RunningCheck(NoCheat plugin) {
        super(plugin, "moving.running");
        this.noFallCheck = new NoFallCheck(plugin);
    }

    public PreciseLocation check(NoCheatPlayer player, MovingData data, MovingConfig cc) {
        PreciseLocation setBack = data.runflySetBackPoint;
        PreciseLocation to = data.to;
        PreciseLocation from = data.from;
        double xDistance = data.to.x - from.x;
        double zDistance = to.z - from.z;
        double horizontalDistance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
        if (!setBack.isSet()) {
            setBack.set(from);
        }

        int fromType = CheckUtil.evaluateLocation(player.getPlayer().getWorld(), from);
        int toType = CheckUtil.evaluateLocation(player.getPlayer().getWorld(), to);
        boolean fromOnGround = CheckUtil.isOnGround(fromType);
        boolean fromInGround = CheckUtil.isInGround(fromType);
        boolean toOnGround = CheckUtil.isOnGround(toType);
        boolean toInGround = CheckUtil.isInGround(toType);
        PreciseLocation newToLocation = null;
        double resultHoriz = Math.max(
            0.0, this.checkHorizontal(player, data, CheckUtil.isLiquid(fromType) && CheckUtil.isLiquid(toType), horizontalDistance, cc)
        );
        double resultVert = Math.max(0.0, this.checkVertical(player, data, fromOnGround, toOnGround, cc));
        double result = (resultHoriz + resultVert) * 100.0;
        data.jumpPhase++;
        data.runflyVL *= 0.95;
        if (result > 0.0) {
            data.runflyVL += result;
            this.incrementStatistics(player, data.statisticCategory, result);
            boolean cancel = this.executeActions(player, cc.actions, data.runflyVL);
            if (cancel) {
                newToLocation = setBack;
            } else if (toOnGround || toInGround) {
                setBack.set(to);
                data.jumpPhase = 0;
            }
        } else if ((!toInGround || !(from.y >= to.y)) && !CheckUtil.isLiquid(toType)) {
            if (!toOnGround || !(from.y >= to.y) && !(setBack.y <= Math.floor(to.y))) {
                if (fromOnGround || fromInGround || toOnGround || toInGround) {
                    data.jumpPhase = 0;
                }
            } else {
                setBack.set(to);
                setBack.y = Math.floor(setBack.y);
                data.jumpPhase = 0;
            }
        } else {
            setBack.set(to);
            setBack.y = Math.ceil(setBack.y);
            data.jumpPhase = 0;
        }

        boolean checkNoFall = cc.nofallCheck && !player.getPlayer().hasPermission("nocheat.checks.moving.nofall");
        if (checkNoFall && newToLocation == null) {
            data.fromOnOrInGround = fromOnGround || fromInGround;
            data.toOnOrInGround = toOnGround || toInGround;
            this.noFallCheck.check(player, data, cc);
        }

        return newToLocation;
    }

    private double checkHorizontal(NoCheatPlayer player, MovingData data, boolean isSwimming, double totalDistance, MovingConfig cc) {
        double distanceAboveLimit = 0.0;
        boolean sprinting = player.isSprinting() && player.getPlayer().getFoodLevel() > 5;
        double limit = 0.0;
        Statistics.Id statisticsCategory = null;
        Block b = player.getPlayer().getLocation().getBlock();
        if (b.getType() == Material.ICE || b.getRelative(0, -1, 0).getType() == Material.ICE) {
            data.onIce = 20;
        } else if (data.onIce > 0) {
            data.onIce--;
        }

        if (cc.sneakingCheck && player.getPlayer().isSneaking() && !player.getPlayer().hasPermission("nocheat.checks.moving.sneaking")) {
            limit = cc.sneakingSpeedLimit;
            statisticsCategory = Statistics.Id.MOV_SNEAKING;
        } else if (isSwimming && !player.getPlayer().hasPermission("nocheat.checks.moving.swimming")) {
            limit = cc.swimmingSpeedLimit;
            statisticsCategory = Statistics.Id.MOV_SWIMMING;
        } else if (!sprinting) {
            limit = cc.walkingSpeedLimit;
            statisticsCategory = Statistics.Id.MOV_RUNNING;
        } else {
            limit = cc.sprintingSpeedLimit;
            statisticsCategory = Statistics.Id.MOV_RUNNING;
        }

        if (data.onIce > 0) {
            limit *= 2.5;
        }

        limit *= player.getSpeedAmplifier();
        distanceAboveLimit = totalDistance - limit - data.horizFreedom;
        data.bunnyhopdelay--;
        if (distanceAboveLimit > 0.0 && sprinting && data.bunnyhopdelay <= 0 && distanceAboveLimit > 0.05 && distanceAboveLimit < 0.4) {
            data.bunnyhopdelay = 9;
            distanceAboveLimit = 0.0;
        }

        if (distanceAboveLimit > 0.0) {
            distanceAboveLimit -= data.horizontalBuffer;
            data.horizontalBuffer = 0.0;
            if (distanceAboveLimit < 0.0) {
                data.horizontalBuffer = -distanceAboveLimit;
            }
        } else {
            data.horizontalBuffer = Math.min(1.0, data.horizontalBuffer - distanceAboveLimit);
        }

        if (distanceAboveLimit > 0.0) {
            data.statisticCategory = statisticsCategory;
        }

        return distanceAboveLimit;
    }

    private double checkVertical(NoCheatPlayer player, MovingData data, boolean fromOnGround, boolean toOnGround, MovingConfig cc) {
        double distanceAboveLimit = 0.0;
        double jumpAmplifier = player.getJumpAmplifier();
        if (jumpAmplifier > data.lastJumpAmplifier) {
            data.lastJumpAmplifier = jumpAmplifier;
        }

        double limit = data.vertFreedom + cc.jumpheight;
        limit *= data.lastJumpAmplifier;
        if (data.jumpPhase > 6.0 + data.lastJumpAmplifier) {
            limit -= (data.jumpPhase - 6) * 0.15;
        }

        distanceAboveLimit = data.to.y - data.runflySetBackPoint.y - limit;
        if (distanceAboveLimit > 0.0) {
            data.statisticCategory = Statistics.Id.MOV_FLYING;
        }

        if (toOnGround || fromOnGround) {
            data.lastJumpAmplifier = 0.0;
        }

        return distanceAboveLimit;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.CHECK) {
            return getData(player).statisticCategory.toString();
        } else {
            return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).runflyVL) : super.getParameter(wildcard, player);
        }
    }
}
