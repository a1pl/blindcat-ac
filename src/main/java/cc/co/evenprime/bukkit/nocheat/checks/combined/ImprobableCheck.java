package cc.co.evenprime.bukkit.nocheat.checks.combined;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class ImprobableCheck extends Check {
    private static ImprobableCheck instance;
    private static final String id = "combined";

    public ImprobableCheck(NoCheat plugin) {
        super(plugin, "combined", "combined.improbable");
        instance = this;
    }

    public static void report(NoCheatPlayer player, double weight) {
        if (instance != null) {
            instance.doReport(player, weight);
        }
    }

    private void doReport(NoCheatPlayer player, double weight) {
        CombinedConfig cc = getConfig(player);
        if (cc.improbableCheck && !player.hasPermission("nocheat.checks.combined.improbable")) {
            CombinedData data = getData(player);
            long now = System.currentTimeMillis();
            if (data.improbableLastTime != 0L) {
                double elapsedSeconds = Math.max(0.0, (now - data.improbableLastTime) / 1000.0);
                data.improbableWeight = data.improbableWeight * Math.pow(0.5, elapsedSeconds);
            }

            data.improbableWeight += weight;
            data.improbableLastTime = now;
            if (data.improbableWeight > cc.improbableLevel) {
                data.improbableVL = data.improbableVL + (data.improbableWeight - cc.improbableLevel);
                this.incrementStatistics(player, Statistics.Id.COMBINED_IMPROBABLE, data.improbableWeight);
                this.executeActions(player, cc.improbableActions, data.improbableVL);
            } else {
                data.improbableVL *= 0.9;
            }
        }
    }

    public static CombinedData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        CombinedData data = base.get("combined");
        if (data == null) {
            data = new CombinedData();
            base.set("combined", data);
        }

        return data;
    }

    public static CombinedConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static CombinedConfig getConfig(ConfigurationCacheStore cache) {
        CombinedConfig config = cache.get("combined");
        if (config == null) {
            config = new CombinedConfig(cache.getConfiguration());
            cache.set("combined", config);
        }

        return config;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).improbableVL) : super.getParameter(wildcard, player);
    }
}
