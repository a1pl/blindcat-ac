package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ReachCheck extends FightCheck {
    public ReachCheck(NoCheat plugin) {
        super(plugin, "fight.reach");
    }

    public boolean check(NoCheatPlayer player, Entity target, FightData data, FightConfig cc) {
        Location eyes = player.getPlayer().getEyeLocation();
        Location t = target.getLocation();
        double half = 0.3;
        double dx = eyes.getX() - clamp(eyes.getX(), t.getX() - 0.3, t.getX() + 0.3);
        double dy = eyes.getY() - clamp(eyes.getY(), t.getY(), t.getY() + hitboxHeight(target));
        double dz = eyes.getZ() - clamp(eyes.getZ(), t.getZ() - 0.3, t.getZ() + 0.3);
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double limit = player.isCreative() ? cc.reachLimit + 2.0 : cc.reachLimit;
        double over = distance - limit;
        long time = System.currentTimeMillis();
        boolean cancel = false;
        if (over <= 0.0) {
            data.reachVL *= 0.7;
        } else {
            data.reachDistance = distance;
            data.reachVL += over * 3.0;
            this.incrementStatistics(player, Statistics.Id.FI_REACH, over);
            ImprobableCheck.report(player, 2.0);
            cancel = this.executeActions(player, cc.reachActions, data.reachVL);
            if (cancel) {
                data.reachLastViolationTime = time;
            }
        }

        if (data.reachLastViolationTime > time) {
            data.reachLastViolationTime = 0L;
        }

        return cancel || data.reachLastViolationTime + cc.reachPenaltyTime > time;
    }

    private static double clamp(double value, double min, double max) {
        return value < min ? min : (value > max ? max : value);
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.VIOLATIONS) {
            return String.format(Locale.US, "%d", (int)getData(player).reachVL);
        } else {
            return wildcard == ParameterName.REACHDISTANCE
                ? String.format(Locale.US, "%.2f", getData(player).reachDistance)
                : super.getParameter(wildcard, player);
        }
    }
}
