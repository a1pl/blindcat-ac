package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class KeepAliveFrequencyCheck extends NetCheck {
    public KeepAliveFrequencyCheck(NoCheat plugin) {
        super(plugin, "net.keepalivefrequency");
    }

    public boolean check(NoCheatPlayer player, NetData data, NetConfig cc) {
        long time = System.currentTimeMillis();
        if (data.lastKeepAliveTime != 0L && time - data.lastKeepAliveTime < cc.keepAliveFrequencyWindowMillis) {
            data.keepAliveBurstCount++;
        } else {
            data.keepAliveBurstCount = 1;
        }

        data.lastKeepAliveTime = time;
        if (data.keepAliveBurstCount <= cc.keepAliveFrequencyBurstLimit) {
            data.keepAliveVL *= 0.7;
            return false;
        } else {
            double over = data.keepAliveBurstCount - cc.keepAliveFrequencyBurstLimit;
            data.keepAliveVL += over;
            this.incrementStatistics(player, Statistics.Id.NET_KEEPALIVEFREQUENCY, over);
            return this.executeActions(player, cc.keepAliveFrequencyActions, data.keepAliveVL);
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).keepAliveVL) : super.getParameter(wildcard, player);
    }
}
