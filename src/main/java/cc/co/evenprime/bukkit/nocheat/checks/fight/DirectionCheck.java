package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.checks.moving.MovingCheck;
import cc.co.evenprime.bukkit.nocheat.checks.moving.MovingData;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DirectionCheck extends FightCheck {
    public DirectionCheck(NoCheat plugin) {
        super(plugin, "fight.direction");
    }

    public boolean check(NoCheatPlayer player, Entity target, FightData data, FightConfig cc) {
        Location t = target.getLocation();
        double height = hitboxHeight(target);
        MovingData movingData = MovingCheck.getData(player);
        double precision = movingData.runflyVL > 10.0 ? cc.directionPrecision * 0.5 : cc.directionPrecision;
        double off = CheckUtil.directionCheck(player, t.getX(), t.getY() + height / 2.0, t.getZ(), 0.6, height, precision);
        long time = System.currentTimeMillis();
        boolean cancel = false;
        if (off < 0.2) {
            data.directionVL *= 0.65;
        } else {
            data.directionVL += off;
            this.incrementStatistics(player, Statistics.Id.FI_DIRECTION, off);
            ImprobableCheck.report(player, 1.5);
            cancel = this.executeActions(player, cc.directionActions, data.directionVL);
            if (cancel) {
                data.directionLastViolationTime = time;
            }
        }

        if (data.directionLastViolationTime > time) {
            data.directionLastViolationTime = 0L;
        }

        return cancel || data.directionLastViolationTime + cc.directionPenaltyTime > time;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).directionVL) : super.getParameter(wildcard, player);
    }
}
