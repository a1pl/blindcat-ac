package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class FlyingFrequencyCheck extends NetCheck {
    public FlyingFrequencyCheck(NoCheat plugin) {
        super(plugin, "net.flyingfrequency");
    }

    public boolean check(NoCheatPlayer player, NetData data, NetConfig cc) {
        long time = System.currentTimeMillis();
        if (data.flyingLastTime == 0L) {
            data.flyingLastTime = time;
            return false;
        }

        data.flyingBuffer--;
        boolean cancel = false;
        if (data.flyingBuffer < 0.0) {
            data.flyingVL = -data.flyingBuffer;
            this.incrementStatistics(player, Statistics.Id.NET_FLYINGFREQUENCY, 1.0);
            cancel = this.executeActions(player, cc.flyingFrequencyActions, data.flyingVL);
        }

        if (data.flyingLastTime + 1000L < time) {
            double seconds = (time - data.flyingLastTime) / 1000.0;
            data.flyingBuffer = data.flyingBuffer + cc.flyingFrequencyPPS * seconds;
            double cap = cc.flyingFrequencyPPS * 2.0;
            if (data.flyingBuffer > cap) {
                data.flyingBuffer = cap;
            }

            data.flyingLastTime = time;
        } else if (data.flyingLastTime > time) {
            data.flyingLastTime = time;
        }

        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).flyingVL) : super.getParameter(wildcard, player);
    }
}
