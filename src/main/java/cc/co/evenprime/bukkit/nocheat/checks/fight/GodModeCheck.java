package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.entity.Player;

public class GodModeCheck extends FightCheck {
    public GodModeCheck(NoCheat plugin) {
        super(plugin, "fight.godmode");
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        boolean cancel = false;
        if (data.godModeLastTime >= System.currentTimeMillis() - 1000L) {
            return false;
        }

        Player bukkitPlayer = player.getPlayer();
        data.godModeLastTime = System.currentTimeMillis();
        int age = bukkitPlayer.getTicksLived();
        int ageDelta = Math.max(0, age - data.godModeLastAge);
        if (bukkitPlayer.getNoDamageTicks() > 0 && ageDelta < 15) {
            data.godModeBuffer -= 15 - ageDelta;
            if (data.godModeBuffer <= 0) {
                data.godModeVL = data.godModeVL - data.godModeBuffer;
                this.incrementStatistics(player, Statistics.Id.FI_GODMODE, data.godModeVL);
                cancel = this.executeActions(player, cc.godModeActions, data.godModeVL);
            }
        } else {
            data.godModeBuffer += 15;
            data.godModeVL *= 0.95;
        }

        if (data.godModeBuffer < 0) {
            data.godModeBuffer = 0;
        } else if (data.godModeBuffer > 30) {
            data.godModeBuffer = 30;
        }

        data.godModeLastAge = age;
        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).godModeVL) : super.getParameter(wildcard, player);
    }
}
