package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class DirectionCheck extends BlockBreakCheck {
    public DirectionCheck(NoCheat plugin) {
        super(plugin, "blockbreak.direction");
    }

    public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig ccblockbreak) {
        SimpleLocation brokenBlock = data.brokenBlockLocation;
        boolean cancel = false;
        double off = CheckUtil.directionCheck(player, brokenBlock.x + 0.5, brokenBlock.y + 0.5, brokenBlock.z + 0.5, 1.0, 1.0, ccblockbreak.directionPrecision);
        long time = System.currentTimeMillis();
        if (off < 0.1) {
            data.directionVL *= 0.9;
        } else {
            if (data.instaBrokenBlockLocation.equals(brokenBlock)) {
                off /= 5.0;
            }

            data.directionVL += off;
            this.incrementStatistics(player, Statistics.Id.BB_DIRECTION, off);
            ImprobableCheck.report(player, 0.3);
            cancel = this.executeActions(player, ccblockbreak.directionActions, data.directionVL);
            if (cancel) {
                data.directionLastViolationTime = time;
            }
        }

        if (data.directionLastViolationTime + ccblockbreak.directionPenaltyTime > time) {
            if (data.directionLastViolationTime > time) {
                data.directionLastViolationTime = 0L;
            }

            return true;
        } else {
            return cancel;
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).directionVL) : super.getParameter(wildcard, player);
    }
}
