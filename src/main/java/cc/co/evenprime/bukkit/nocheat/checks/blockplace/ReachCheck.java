package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class ReachCheck extends BlockPlaceCheck {
    public ReachCheck(NoCheat plugin) {
        super(plugin, "blockplace.reach");
    }

    public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc) {
        boolean cancel = false;
        SimpleLocation placedAgainstBlock = data.blockPlacedAgainst;
        double distance = CheckUtil.reachCheck(
            player,
            placedAgainstBlock.x + 0.5,
            placedAgainstBlock.y + 0.5,
            placedAgainstBlock.z + 0.5,
            player.isCreative() ? cc.reachDistance + 2.0 : cc.reachDistance
        );
        if (distance <= 0.0) {
            data.reachVL *= 0.9;
        } else {
            data.reachVL += distance;
            this.incrementStatistics(player, Statistics.Id.BP_REACH, distance);
            data.reachdistance = distance;
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
                ? String.format(Locale.US, "%.2f", getData(player).reachdistance)
                : super.getParameter(wildcard, player);
        }
    }
}
