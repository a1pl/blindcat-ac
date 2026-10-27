package cc.co.evenprime.bukkit.nocheat.checks.chat;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;
import java.util.LinkedList;
import java.util.List;

public class ChatConfig implements ConfigItem {
    public final boolean spamCheck;
    public final String[] spamWhitelist;
    public final long spamTimeframe;
    public final int spamMessageLimit;
    public final int spamCommandLimit;
    public final ActionList spamActions;
    public final boolean colorCheck;
    public final ActionList colorActions;

    public ChatConfig(NoCheatConfiguration data) {
        this.spamCheck = data.getBoolean("checks.chat.spam.active");
        this.spamWhitelist = this.splitWhitelist(data.getString("checks.chat.spam.whitelist"));
        this.spamTimeframe = data.getInt("checks.chat.spam.timeframe") * 1000L;
        this.spamMessageLimit = data.getInt("checks.chat.spam.messagelimit");
        this.spamCommandLimit = data.getInt("checks.chat.spam.commandlimit");
        this.spamActions = data.getActionList("checks.chat.spam.actions", "nocheat.checks.chat.spam");
        this.colorCheck = data.getBoolean("checks.chat.color.active");
        this.colorActions = data.getActionList("checks.chat.color.actions", "nocheat.checks.chat.color");
    }

    private String[] splitWhitelist(String string) {
        List<String> strings = new LinkedList<>();
        string = string.trim();

        for (String s : string.split(",")) {
            if (s != null && s.trim().length() > 0) {
                strings.add(s.trim());
            }
        }

        return strings.toArray(new String[strings.size()]);
    }
}
