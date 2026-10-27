package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class FlyingCheck extends MovingCheck {
    private static final double creativeSpeed = 0.6;

    public FlyingCheck(NoCheat plugin) {
        super(plugin, "moving.flying");
    }

    public PreciseLocation check(NoCheatPlayer player, MovingData data, MovingConfig ccmoving) {
        PreciseLocation setBack = data.runflySetBackPoint;
        PreciseLocation from = data.from;
        PreciseLocation to = data.to;
        if (!setBack.isSet()) {
            setBack.set(from);
        }

        PreciseLocation newToLocation = null;
        int maxheight = ccmoving.flyingHeightLimit + player.getPlayer().getWorld().getMaxHeight();
        if (to.y - data.vertFreedom > maxheight) {
            newToLocation = new PreciseLocation();
            newToLocation.set(setBack);
            newToLocation.y = maxheight - 10;
            return newToLocation;
        }

        double yDistance = to.y - from.y;
        double xDistance = to.x - from.x;
        double zDistance = to.z - from.z;
        double horizontalDistance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
        double resultHoriz = 0.0;
        double resultVert = 0.0;
        double result = 0.0;
        double speedLimitHorizontal = player.isCreative() ? Math.max(0.6, ccmoving.flyingSpeedLimitHorizontal) : ccmoving.flyingSpeedLimitHorizontal;
        speedLimitHorizontal *= player.getSpeedAmplifier();
        resultHoriz = Math.max(0.0, horizontalDistance - data.horizFreedom - speedLimitHorizontal);
        boolean sprinting = player.isSprinting();
        data.bunnyhopdelay--;
        if (resultHoriz > 0.0 && sprinting && data.bunnyhopdelay <= 0 && resultHoriz < 0.4) {
            data.bunnyhopdelay = 9;
            resultHoriz = 0.0;
        }

        resultHoriz *= 100.0;
        double jumpAmplifier = player.getJumpAmplifier();
        if (jumpAmplifier > data.lastJumpAmplifier) {
            data.lastJumpAmplifier = jumpAmplifier;
        }

        double speedLimitVertical = ccmoving.flyingSpeedLimitVertical * data.lastJumpAmplifier;
        if (data.from.y >= data.to.y && data.lastJumpAmplifier > 0.0) {
            data.lastJumpAmplifier--;
        }

        resultVert = Math.max(0.0, yDistance - data.vertFreedom - speedLimitVertical) * 100.0;
        result = resultHoriz + resultVert;
        if (result > 0.0) {
            data.runflyVL += result;
            if (resultHoriz > 0.0) {
                this.incrementStatistics(player, Statistics.Id.MOV_RUNNING, resultHoriz);
            }

            if (resultVert > 0.0) {
                this.incrementStatistics(player, Statistics.Id.MOV_FLYING, resultVert);
            }

            boolean cancel = this.executeActions(player, ccmoving.flyingActions, data.runflyVL);
            if (cancel) {
                newToLocation = setBack;
            }
        }

        data.runflyVL *= 0.97;
        if (newToLocation == null) {
            setBack.set(to);
        }

        return newToLocation;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).runflyVL) : super.getParameter(wildcard, player);
    }
}
