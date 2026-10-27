package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;

public abstract class InventoryCheck extends Check {
    private static final String id = "inventory";

    public InventoryCheck(NoCheat plugin, String name) {
        super(plugin, "inventory", name);
    }

    public static InventoryData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        InventoryData data = base.get("inventory");
        if (data == null) {
            data = new InventoryData();
            base.set("inventory", data);
        }

        return data;
    }

    public static InventoryConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static InventoryConfig getConfig(ConfigurationCacheStore cache) {
        InventoryConfig config = cache.get("inventory");
        if (config == null) {
            config = new InventoryConfig(cache.getConfiguration());
            cache.set("inventory", config);
        }

        return config;
    }
}
