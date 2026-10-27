package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class FastBreakCheck extends BlockBreakCheck {
    public FastBreakCheck(NoCheat plugin) {
        super(plugin, "blockbreak.fastbreak");
    }

    public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig cc) {
        long time = System.currentTimeMillis();
        boolean instaBreak = data.instaBrokenBlockLocation.equals(data.brokenBlockLocation);
        boolean cancel = false;
        if (!instaBreak && data.lastBreakTime != 0L) {
            long elapsed = time - data.lastBreakTime;
            if (elapsed < cc.fastBreakDelay) {
                double off = cc.fastBreakDelay - elapsed;
                data.fastBreakVL += off;
                this.incrementStatistics(player, Statistics.Id.BB_FASTBREAK, off);
                cancel = this.executeActions(player, cc.fastBreakActions, data.fastBreakVL);
            } else {
                data.fastBreakVL *= 0.8;
            }
        }

        data.lastBreakTime = time;
        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).fastBreakVL) : super.getParameter(wildcard, player);
    }
}
