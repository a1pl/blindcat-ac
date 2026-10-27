package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class DropCheck extends InventoryCheck {
    public DropCheck(NoCheat plugin) {
        super(plugin, "inventory.drop");
    }

    public boolean check(NoCheatPlayer player, InventoryData data, InventoryConfig cc) {
        boolean cancel = false;
        long time = System.currentTimeMillis();
        if (data.dropLastTime + cc.dropTimeFrame <= time) {
            data.dropLastTime = time;
            data.dropCount = 0;
            data.dropVL = 0;
        } else if (data.dropLastTime > time) {
            data.dropLastTime = -2147483648L;
        }

        data.dropCount++;
        if (data.dropCount > cc.dropLimit) {
            data.dropVL = data.dropCount - cc.dropLimit;
            this.incrementStatistics(player, Statistics.Id.INV_DROP, 1.0);
            cancel = this.executeActions(player, cc.dropActions, data.dropVL);
        }

        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", getData(player).dropVL) : super.getParameter(wildcard, player);
    }
}
