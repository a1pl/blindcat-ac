package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class ReachCheck extends BlockBreakCheck {
    public ReachCheck(NoCheat plugin) {
        super(plugin, "blockbreak.reach");
    }

    public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig cc) {
        boolean cancel = false;
        SimpleLocation brokenBlock = data.brokenBlockLocation;
        double distance = CheckUtil.reachCheck(
            player, brokenBlock.x + 0.5, brokenBlock.y + 0.5, brokenBlock.z + 0.5, player.isCreative() ? cc.reachDistance + 2.0 : cc.reachDistance
        );
        if (distance <= 0.0) {
            data.reachVL *= 0.9;
        } else {
            data.reachVL += distance;
            this.incrementStatistics(player, Statistics.Id.BB_REACH, distance);
            data.reachDistance = distance;
            cancel = this.executeActions(player, cc.reachActions, data.reachVL);
        }

        return cancel;
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
