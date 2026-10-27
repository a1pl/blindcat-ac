package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public abstract class FightCheck extends Check {
    private static final String id = "fight";
    protected static final double HITBOX_WIDTH = 0.6;

    public FightCheck(NoCheat plugin, String name) {
        super(plugin, "fight", name);
    }

    protected static double hitboxHeight(Entity entity) {
        return entity instanceof LivingEntity ? ((LivingEntity)entity).getEyeHeight() / 0.85 : 1.0;
    }

    public static FightData getData(NoCheatPlayer player) {
        DataStore base = player.getDataStore();
        FightData data = base.get("fight");
        if (data == null) {
            data = new FightData();
            base.set("fight", data);
        }

        return data;
    }

    public static FightConfig getConfig(NoCheatPlayer player) {
        return getConfig(player.getConfigurationStore());
    }

    public static FightConfig getConfig(ConfigurationCacheStore cache) {
        FightConfig config = cache.get("fight");
        if (config == null) {
            config = new FightConfig(cache.getConfiguration());
            cache.set("fight", config);
        }

        return config;
    }
}
