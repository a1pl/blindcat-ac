package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;

public abstract class BlockBreakCheck extends Check {
    private static final String id = "blockbreak";

    public BlockBreakCheck(NoCheat plugin, String name) {
        super(plugin, "blockbreak", name);
    }

    public static BlockBreakData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        BlockBreakData data = base.get("blockbreak");
        if (data == null) {
            data = new BlockBreakData();
            base.set("blockbreak", data);
        }

        return data;
    }

    public static BlockBreakConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static BlockBreakConfig getConfig(ConfigurationCacheStore cache) {
        BlockBreakConfig config = cache.get("blockbreak");
        if (config == null) {
            config = new BlockBreakConfig(cache.getConfiguration());
            cache.set("blockbreak", config);
        }

        return config;
    }
}
