package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.entity.Entity;

public class SelfHitCheck extends FightCheck {
    public SelfHitCheck(NoCheat plugin) {
        super(plugin, "fight.selfhit");
    }

    public boolean check(NoCheatPlayer attacker, Entity target, FightData data, FightConfig cc) {
        if (attacker.getPlayer().getEntityId() != target.getEntityId()) {
            return false;
        }

        data.selfHitVL++;
        this.incrementStatistics(attacker, Statistics.Id.FI_SELFHIT, 1.0);
        boolean cancel = this.executeActions(attacker, cc.selfHitActions, data.selfHitVL);
        data.selfHitVL *= 0.99;
        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).selfHitVL) : super.getParameter(wildcard, player);
    }
}
