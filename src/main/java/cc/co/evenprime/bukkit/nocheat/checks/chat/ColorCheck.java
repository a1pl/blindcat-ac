package cc.co.evenprime.bukkit.nocheat.checks.chat;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class ColorCheck extends ChatCheck {
    public ColorCheck(NoCheat plugin) {
        super(plugin, "chat.color");
    }

    public boolean check(NoCheatPlayer player, ChatData data, ChatConfig cc) {
        if (data.message.matches(".*§.*")) {
            data.colorVL++;
            this.incrementStatistics(player, Statistics.Id.CHAT_COLOR, 1.0);
            boolean filter = this.executeActions(player, cc.colorActions, data.colorVL);
            if (filter) {
                data.message = data.message.replaceAll("Â§.", "").replaceAll("§.", "");
            }
        }

        return false;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", getData(player).colorVL) : super.getParameter(wildcard, player);
    }
}
