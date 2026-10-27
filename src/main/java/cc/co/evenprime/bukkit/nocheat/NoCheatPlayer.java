package cc.co.evenprime.bukkit.nocheat;

import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;
import cc.co.evenprime.bukkit.nocheat.data.ExecutionHistory;
import org.bukkit.entity.Player;

public interface NoCheatPlayer {
    boolean hasPermission(String var1);

    String getName();

    Player getPlayer();

    DataStore getDataStore();

    boolean isDead();

    boolean isSprinting();

    int getTicksLived();

    ConfigurationCacheStore getConfigurationStore();

    float getSpeedAmplifier();

    float getJumpAmplifier();

    boolean isCreative();

    ExecutionHistory getExecutionHistory();

    void dealFallDamage();
}
