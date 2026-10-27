package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class MorePacketsCheck extends MovingCheck {
    private static final int packetsPerTimeframe = 22;

    public MorePacketsCheck(NoCheat plugin) {
        super(plugin, "moving.morepackets");
    }

    public PreciseLocation check(NoCheatPlayer player, MovingData data, MovingConfig cc) {
        PreciseLocation newToLocation = null;
        if (!data.morePacketsSetbackPoint.isSet()) {
            data.morePacketsSetbackPoint.set(data.from);
        }

        long time = System.currentTimeMillis();
        data.morePacketsBuffer--;
        if (data.morePacketsBuffer < 0) {
            data.morePacketsVL = -data.morePacketsBuffer;
            this.incrementStatistics(player, Statistics.Id.MOV_MOREPACKETS, 1.0);
            data.packets = -data.morePacketsBuffer;
            boolean cancel = this.executeActions(player, cc.morePacketsActions, data.morePacketsVL);
            if (cancel) {
                newToLocation = data.morePacketsSetbackPoint;
            }
        }

        if (data.morePacketsLastTime + 1000L < time) {
            double seconds = (time - data.morePacketsLastTime) / 1000.0;
            data.morePacketsBuffer = (int)(data.morePacketsBuffer + 22.0 * seconds);
            if (seconds > 2.0) {
                if (data.morePacketsBuffer > 100) {
                    data.morePacketsBuffer = 100;
                }
            } else if (data.morePacketsBuffer > 50) {
                data.morePacketsBuffer = 50;
            }

            data.morePacketsLastTime = time;
            if (newToLocation == null) {
                data.morePacketsSetbackPoint.set(data.from);
            }
        } else if (data.morePacketsLastTime > time) {
            data.morePacketsLastTime = time;
        }

        return newToLocation;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.VIOLATIONS) {
            return String.format(Locale.US, "%d", (int)getData(player).morePacketsVL);
        } else {
            return wildcard == ParameterName.PACKETS ? String.format(Locale.US, "%d", getData(player).packets) : super.getParameter(wildcard, player);
        }
    }
}
