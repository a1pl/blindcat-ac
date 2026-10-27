package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;

public class DirectionCheck extends BlockPlaceCheck {
    private static final double SIDE_TOLERANCE = 0.25;

    public DirectionCheck(NoCheat plugin) {
        super(plugin, "blockplace.direction");
    }

    public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc) {
        boolean cancel = false;
        SimpleLocation blockPlaced = data.blockPlaced;
        SimpleLocation blockPlacedAgainst = data.blockPlacedAgainst;
        double off = CheckUtil.directionCheck(
            player, blockPlacedAgainst.x + 0.5, blockPlacedAgainst.y + 0.5, blockPlacedAgainst.z + 0.5, 1.0, 1.0, cc.directionPrecision
        );
        double off2 = 0.0;
        Location eyes = player.getPlayer().getEyeLocation();
        if (blockPlaced.x > blockPlacedAgainst.x) {
            off2 = blockPlacedAgainst.x + 0.5 - eyes.getX();
        } else if (blockPlaced.x < blockPlacedAgainst.x) {
            off2 = -(blockPlacedAgainst.x + 0.5 - eyes.getX());
        } else if (blockPlaced.y > blockPlacedAgainst.y) {
            off2 = blockPlacedAgainst.y + 0.5 - eyes.getY();
        } else if (blockPlaced.y < blockPlacedAgainst.y) {
            off2 = -(blockPlacedAgainst.y + 0.5 - eyes.getY());
        } else if (blockPlaced.z > blockPlacedAgainst.z) {
            off2 = blockPlacedAgainst.z + 0.5 - eyes.getZ();
        } else if (blockPlaced.z < blockPlacedAgainst.z) {
            off2 = -(blockPlacedAgainst.z + 0.5 - eyes.getZ());
        }

        if (off2 > 0.25) {
            off += off2 - 0.25;
        }

        long time = System.currentTimeMillis();
        if (off < 0.1) {
            data.directionVL *= 0.9;
        } else {
            data.directionVL += off;
            this.incrementStatistics(player, Statistics.Id.BP_DIRECTION, off);
            ImprobableCheck.report(player, 0.3);
            cancel = this.executeActions(player, cc.directionActions, data.directionVL);
            if (cancel) {
                data.directionLastViolationTime = time;
            }
        }

        if (data.directionLastViolationTime + cc.directionPenaltyTime > time) {
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
