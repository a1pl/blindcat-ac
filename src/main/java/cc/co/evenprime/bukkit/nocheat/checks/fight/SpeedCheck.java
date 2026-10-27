package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class SpeedCheck extends FightCheck {
    public SpeedCheck(NoCheat plugin) {
        super(plugin, "fight.speed");
    }

    public void swing(FightData data) {
        long time = System.currentTimeMillis();
        if (data.speedTime + 1000L <= time || data.speedTime > time) {
            data.speedTime = time;
            data.speedAttackCount = 0;
        }

        data.speedAttackCount++;
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        if (data.speedAttackCount <= cc.speedAttackLimit) {
            data.speedVL *= 0.8;
            return false;
        } else {
            double over = data.speedAttackCount - cc.speedAttackLimit;
            data.speedVL += over;
            this.incrementStatistics(player, Statistics.Id.FI_SPEED, over);
            ImprobableCheck.report(player, 1.0);
            return this.executeActions(player, cc.speedActions, data.speedVL);
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.VIOLATIONS) {
            return String.format(Locale.US, "%d", (int)getData(player).speedVL);
        } else {
            return wildcard == ParameterName.LIMIT ? String.format(Locale.US, "%d", getData(player).speedAttackCount) : super.getParameter(wildcard, player);
        }
    }
}
