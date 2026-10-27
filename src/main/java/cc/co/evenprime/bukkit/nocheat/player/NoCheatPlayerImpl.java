package cc.co.evenprime.bukkit.nocheat.player;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;
import cc.co.evenprime.bukkit.nocheat.data.ExecutionHistory;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoCheatPlayerImpl implements NoCheatPlayer {
    private Player player;
    private final NoCheat plugin;
    private final DataStore data;
    private ConfigurationCacheStore config;
    private long lastUsedTime;
    private final ExecutionHistory history;

    public NoCheatPlayerImpl(Player player, NoCheat plugin) {
        this.player = player;
        this.plugin = plugin;
        this.data = new DataStore();
        this.history = new ExecutionHistory();
        this.lastUsedTime = System.currentTimeMillis();
    }

    public void refresh(Player player) {
        this.player = player;
        this.config = this.plugin.getConfig(player);
    }

    @Override
    public boolean isDead() {
        return this.player.getHealth() <= 0.0 || this.player.isDead();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public DataStore getDataStore() {
        return this.data;
    }

    @Override
    public ConfigurationCacheStore getConfigurationStore() {
        return this.config;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public int getTicksLived() {
        return this.player.getTicksLived();
    }

    @Override
    public float getSpeedAmplifier() {
        int amp = this.getAmplifier(PotionEffectType.SPEED);
        return amp < 0 ? 1.0F : 1.0F + 0.2F * (amp + 1);
    }

    @Override
    public float getJumpAmplifier() {
        int amp = this.getAmplifier(PotionEffectType.JUMP);
        return amp < 0 ? 1.0F : (amp > 20 ? 1.5F : 1.2F) * (amp + 1);
    }

    private int getAmplifier(PotionEffectType type) {
        for (PotionEffect effect : this.player.getActivePotionEffects()) {
            if (effect.getType().equals(type)) {
                return effect.getAmplifier();
            }
        }

        return -1;
    }

    @Override
    public boolean isSprinting() {
        return this.player.isSprinting();
    }

    public void setLastUsedTime(long currentTimeInMilliseconds) {
        this.lastUsedTime = currentTimeInMilliseconds;
    }

    public boolean shouldBeRemoved(long currentTimeInMilliseconds) {
        if (this.lastUsedTime > currentTimeInMilliseconds) {
            this.lastUsedTime = currentTimeInMilliseconds;
        }

        return this.lastUsedTime + 60000L < currentTimeInMilliseconds;
    }

    @Override
    public boolean isCreative() {
        return this.player.getGameMode() == GameMode.CREATIVE;
    }

    @Override
    public ExecutionHistory getExecutionHistory() {
        return this.history;
    }

    @Override
    public void dealFallDamage() {
        float fallDistance = this.player.getFallDistance();
        double damage = Math.ceil(fallDistance - 3.0F);
        if (damage > 0.0) {
            this.player.damage(damage);
        }

        this.player.setFallDistance(0.0F);
    }
}
