package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class NoswingCheck extends FightCheck {
    public NoswingCheck(NoCheat plugin) {
        super(plugin, "fight.noswing");
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        if (data.armswung) {
            data.armswung = false;
            data.noswingVL *= 0.8;
            return false;
        } else {
            data.noswingVL += 0.6;
            this.incrementStatistics(player, Statistics.Id.FI_NOSWING, 1.0);
            ImprobableCheck.report(player, 0.5);
            return this.executeActions(player, cc.noswingActions, data.noswingVL);
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).noswingVL) : super.getParameter(wildcard, player);
    }
}
