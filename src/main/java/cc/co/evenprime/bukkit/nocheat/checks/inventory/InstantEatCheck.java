package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class InstantEatCheck extends InventoryCheck {
    public InstantEatCheck(NoCheat plugin) {
        super(plugin, "inventory.instanteat");
    }

    public boolean check(NoCheatPlayer player, FoodLevelChangeEvent event, InventoryData data, InventoryConfig cc) {
        if (data.foodMaterial != null && event.getFoodLevel() > player.getPlayer().getFoodLevel()) {
            boolean cancelled = false;
            long time = System.currentTimeMillis();
            long expectedTimeWhenEatingFinished = data.lastEatInteractTime + 700L;
            if (expectedTimeWhenEatingFinished < time) {
                data.instantEatVL *= 0.6;
            } else if (data.lastEatInteractTime > time) {
                data.lastEatInteractTime = 0L;
            } else {
                int vl = (int)(expectedTimeWhenEatingFinished - time) / 100;
                data.instantEatVL += vl;
                this.incrementStatistics(player, Statistics.Id.INV_EAT, vl);
                cancelled = this.executeActions(player, cc.eatActions, data.instantEatVL);
            }

            return cancelled;
        } else {
            return false;
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.VIOLATIONS) {
            return String.format(Locale.US, "%d", (int)getData(player).instantEatVL);
        } else {
            return wildcard == ParameterName.FOOD ? getData(player).foodMaterial.toString() : super.getParameter(wildcard, player);
        }
    }
}
