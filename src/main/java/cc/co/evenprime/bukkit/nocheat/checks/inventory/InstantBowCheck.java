package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.event.entity.EntityShootBowEvent;

public class InstantBowCheck extends InventoryCheck {
    public InstantBowCheck(NoCheat plugin) {
        super(plugin, "inventory.instantbow");
    }

    public boolean check(NoCheatPlayer player, EntityShootBowEvent event, InventoryData data, InventoryConfig cc) {
        boolean cancelled = false;
        long time = System.currentTimeMillis();
        float bowForce = event.getForce();
        long expectedTimeWhenStringDrawn = data.lastBowInteractTime + (int)(bowForce * bowForce * 700.0F);
        if (expectedTimeWhenStringDrawn < time) {
            data.instantBowVL = (int)(data.instantBowVL * 0.9);
        } else if (data.lastBowInteractTime > time) {
            data.lastBowInteractTime = 0L;
        } else {
            int vl = (int)(expectedTimeWhenStringDrawn - time) / 100;
            data.instantBowVL += vl;
            this.incrementStatistics(player, Statistics.Id.INV_BOW, vl);
            cancelled = this.executeActions(player, cc.bowActions, data.instantBowVL);
        }

        return cancelled;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", getData(player).instantBowVL) : super.getParameter(wildcard, player);
    }
}
