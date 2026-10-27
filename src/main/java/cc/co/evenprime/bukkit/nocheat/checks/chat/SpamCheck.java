package cc.co.evenprime.bukkit.nocheat.checks.chat;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class SpamCheck extends ChatCheck {
    public SpamCheck(NoCheat plugin) {
        super(plugin, "chat.spam");
    }

    public boolean check(NoCheatPlayer player, ChatData data, ChatConfig cc) {
        boolean cancel = false;

        for (String s : cc.spamWhitelist) {
            if (data.message.startsWith(s)) {
                return false;
            }
        }

        int commandLimit = cc.spamCommandLimit;
        int messageLimit = cc.spamMessageLimit;
        long timeframe = cc.spamTimeframe;
        long time = System.currentTimeMillis();
        if (data.spamLastTime + timeframe <= time) {
            data.spamLastTime = time;
            data.messageCount = 0;
            data.commandCount = 0;
        } else if (data.spamLastTime > time) {
            data.spamLastTime = -2147483648L;
        }

        if (data.message.startsWith("/")) {
            data.commandCount++;
        } else {
            data.messageCount++;
        }

        if (data.messageCount > messageLimit || data.commandCount > commandLimit) {
            data.spamVL = Math.max(0, data.messageCount - messageLimit);
            data.spamVL = data.spamVL + Math.max(0, data.commandCount - commandLimit);
            this.incrementStatistics(player, Statistics.Id.CHAT_SPAM, 1.0);
            cancel = this.executeActions(player, cc.spamActions, data.spamVL);
        }

        return cancel;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", getData(player).spamVL) : super.getParameter(wildcard, player);
    }
}
