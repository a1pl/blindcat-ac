package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class NoswingCheck extends BlockBreakCheck {
    public NoswingCheck(NoCheat plugin) {
        super(plugin, "blockbreak.noswing");
    }

    public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig cc) {
        boolean cancel = false;
        if (data.armswung) {
            data.armswung = false;
            data.noswingVL *= 0.9;
        } else {
            data.noswingVL++;
            this.incrementStatistics(player, Statistics.Id.BB_NOSWING, 1.0);
            cancel = this.executeActions(player, cc.noswingActions, data.noswingVL);
        }

        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).noswingVL) : super.getParameter(wildcard, player);
    }
}
