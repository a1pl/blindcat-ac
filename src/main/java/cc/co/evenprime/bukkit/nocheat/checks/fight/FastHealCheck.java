package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class FastHealCheck extends FightCheck {
    public FastHealCheck(NoCheat plugin) {
        super(plugin, "fight.fastheal");
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        long time = System.currentTimeMillis();
        boolean cancel = false;
        if (time >= data.fastHealRefTime && time - data.fastHealRefTime < cc.fastHealInterval) {
            double diff = time - data.fastHealRefTime;
            if (diff < cc.fastHealInterval) {
                data.fastHealBuffer = (long)(data.fastHealBuffer - (cc.fastHealInterval - diff));
                if (data.fastHealBuffer <= 0L) {
                    double violation = (cc.fastHealInterval - diff) / 1000.0;
                    data.fastHealVL += violation;
                    this.incrementStatistics(player, Statistics.Id.FI_FASTHEAL, violation);
                    cancel = this.executeActions(player, cc.fastHealActions, data.fastHealVL);
                }
            }
        } else {
            data.fastHealVL *= 0.96;
            data.fastHealBuffer = Math.min(cc.fastHealBuffer, data.fastHealBuffer + 50L);
        }

        data.fastHealRefTime = time;
        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).fastHealVL) : super.getParameter(wildcard, player);
    }
}
