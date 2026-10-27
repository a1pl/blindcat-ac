package cc.co.evenprime.bukkit.nocheat.config;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationCacheStore {
    public final LoggingConfig logging;
    private final Map<String, ConfigItem> configMap = new HashMap<>();
    private final NoCheatConfiguration data;

    public ConfigurationCacheStore(NoCheatConfiguration data) {
        this.logging = new LoggingConfig(data);
        this.data = data;
    }

    public <T extends ConfigItem> T get(String id) {
        return (T)this.configMap.get(id);
    }

    public void set(String id, ConfigItem config) {
        this.configMap.put(id, config);
    }

    public NoCheatConfiguration getConfiguration() {
        return this.data;
    }
}
