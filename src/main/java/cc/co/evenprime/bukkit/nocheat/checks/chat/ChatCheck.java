package cc.co.evenprime.bukkit.nocheat.checks.chat;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;

public abstract class ChatCheck extends Check {
    private static final String id = "chat";

    public ChatCheck(NoCheat plugin, String name) {
        super(plugin, "chat", name);
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.TEXT ? getData(player).message.replaceAll("Â§.", "").replaceAll("§.", "") : super.getParameter(wildcard, player);
    }

    public static ChatData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        ChatData data = base.get("chat");
        if (data == null) {
            data = new ChatData();
            base.set("chat", data);
        }

        return data;
    }

    public static ChatConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static ChatConfig getConfig(ConfigurationCacheStore cache) {
        ChatConfig config = cache.get("chat");
        if (config == null) {
            config = new ChatConfig(cache.getConfiguration());
            cache.set("chat", config);
        }

        return config;
    }
}
