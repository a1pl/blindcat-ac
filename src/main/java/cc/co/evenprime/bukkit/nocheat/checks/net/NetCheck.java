package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;

public abstract class NetCheck extends Check {
    private static final String id = "net";

    public NetCheck(NoCheat plugin, String name) {
        super(plugin, "net", name);
    }

    public static NetData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        NetData data = base.get("net");
        if (data == null) {
            data = new NetData();
            base.set("net", data);
        }

        return data;
    }

    public static NetConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static NetConfig getConfig(ConfigurationCacheStore cache) {
        NetConfig config = cache.get("net");
        if (config == null) {
            config = new NetConfig(cache.getConfiguration());
            cache.set("net", config);
        }

        return config;
    }
}
