package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class FastPlaceCheck extends BlockPlaceCheck {
    public FastPlaceCheck(NoCheat plugin) {
        super(plugin, "blockplace.fastplace");
    }

    public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc) {
        long time = System.currentTimeMillis();
        boolean cancel = false;
        if (data.lastPlaceTime != 0L) {
            long elapsed = time - data.lastPlaceTime;
            if (elapsed < cc.fastPlaceDelay) {
                double off = cc.fastPlaceDelay - elapsed;
                data.fastPlaceVL += off;
                this.incrementStatistics(player, Statistics.Id.BP_FASTPLACE, off);
                cancel = this.executeActions(player, cc.fastPlaceActions, data.fastPlaceVL);
            } else {
                data.fastPlaceVL *= 0.8;
            }
        }

        data.lastPlaceTime = time;
        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).fastPlaceVL) : super.getParameter(wildcard, player);
    }
}
