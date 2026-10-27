package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class AttackFrequencyCheck extends NetCheck {
    public AttackFrequencyCheck(NoCheat plugin) {
        super(plugin, "net.attackfrequency");
    }

    public boolean check(NoCheatPlayer player, NetData data, NetConfig cc) {
        long time = System.currentTimeMillis();
        if (time - data.attackWindowStart > 1000L) {
            data.attackWindowStart = time;
            data.attackWindowCount = 0;
        }

        data.attackWindowCount++;
        if (data.attackWindowCount <= cc.attackFrequencyLimit) {
            data.attackVL *= 0.8;
            return false;
        } else {
            double over = data.attackWindowCount - cc.attackFrequencyLimit;
            data.attackVL += over;
            this.incrementStatistics(player, Statistics.Id.NET_ATTACKFREQUENCY, over);
            return this.executeActions(player, cc.attackFrequencyActions, data.attackVL);
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).attackVL) : super.getParameter(wildcard, player);
    }
}
