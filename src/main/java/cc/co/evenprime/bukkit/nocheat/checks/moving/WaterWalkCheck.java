package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.World;

public class WaterWalkCheck extends MovingCheck {
    public WaterWalkCheck(NoCheat plugin) {
        super(plugin, "moving.waterwalk");
    }

    public void check(NoCheatPlayer player, MovingData data, MovingConfig cc) {
        World world = player.getPlayer().getWorld();
        boolean onLiquid = CheckUtil.isLiquid(CheckUtil.evaluateLocation(world, data.to));
        if (onLiquid && !player.getPlayer().isInsideVehicle()) {
            double yDelta = Math.abs(data.to.y - data.from.y);
            double horizDelta = Math.sqrt(Math.pow(data.to.x - data.from.x, 2.0) + Math.pow(data.to.z - data.from.z, 2.0));
            if (horizDelta > 0.02 && yDelta < 0.003) {
                data.waterWalkTicks++;
            } else {
                data.waterWalkTicks = Math.max(0, data.waterWalkTicks - 2);
            }

            if (data.waterWalkTicks > cc.waterwalkTicks) {
                data.waterwalkVL++;
                this.incrementStatistics(player, Statistics.Id.MOV_WATERWALK, 1.0);
                ImprobableCheck.report(player, 0.5);
                this.executeActions(player, cc.waterwalkActions, data.waterwalkVL);
                data.waterWalkTicks = 0;
            } else {
                data.waterwalkVL *= 0.95;
            }
        } else {
            data.waterWalkTicks = 0;
            data.waterwalkVL *= 0.9;
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).waterwalkVL) : super.getParameter(wildcard, player);
    }
}
