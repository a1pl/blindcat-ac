package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.World;

public class PassableCheck extends MovingCheck {
    public PassableCheck(NoCheat plugin) {
        super(plugin, "moving.passable");
    }

    public PreciseLocation check(NoCheatPlayer player, MovingData data, MovingConfig cc) {
        PreciseLocation from = data.from;
        PreciseLocation to = data.to;
        World world = player.getPlayer().getWorld();
        if (!CheckUtil.isInsideBlock(world, to)) {
            data.passableVL *= 0.99;
            return null;
        } else if (Location.locToBlock(from.x) == Location.locToBlock(to.x)
            && Location.locToBlock(from.y) == Location.locToBlock(to.y)
            && Location.locToBlock(from.z) == Location.locToBlock(to.z)
            && CheckUtil.isInsideBlock(world, from)) {
            return null;
        } else {
            data.passableVL++;
            this.incrementStatistics(player, Statistics.Id.MOV_PASSABLE, 1.0);
            boolean cancel = this.executeActions(player, cc.passableActions, data.passableVL);
            if (cancel) {
                PreciseLocation newTo = new PreciseLocation();
                newTo.set(from);
                return newTo;
            } else {
                return null;
            }
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).passableVL) : super.getParameter(wildcard, player);
    }
}
