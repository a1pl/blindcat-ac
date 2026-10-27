package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;
import java.util.Locale;

public abstract class BlockPlaceCheck extends Check {
    private static final String id = "blockplace";

    public BlockPlaceCheck(NoCheat plugin, String name) {
        super(plugin, "blockplace", name);
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.PLACE_LOCATION) {
            SimpleLocation l = getData(player).blockPlaced;
            return l.isSet() ? String.format(Locale.US, "%d %d %d", l.x, l.y, l.z) : "null";
        } else if (wildcard == ParameterName.PLACE_AGAINST) {
            SimpleLocation l = getData(player).blockPlacedAgainst;
            return l.isSet() ? String.format(Locale.US, "%d %d %d", l.x, l.y, l.z) : "null";
        } else {
            return super.getParameter(wildcard, player);
        }
    }

    public static BlockPlaceData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        BlockPlaceData data = base.get("blockplace");
        if (data == null) {
            data = new BlockPlaceData();
            base.set("blockplace", data);
        }

        return data;
    }

    public static BlockPlaceConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static BlockPlaceConfig getConfig(ConfigurationCacheStore cache) {
        BlockPlaceConfig config = cache.get("blockplace");
        if (config == null) {
            config = new BlockPlaceConfig(cache.getConfiguration());
            cache.set("blockplace", config);
        }

        return config;
    }
}
