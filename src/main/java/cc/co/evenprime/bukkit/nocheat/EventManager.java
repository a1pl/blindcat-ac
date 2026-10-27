package cc.co.evenprime.bukkit.nocheat;

import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.List;
import org.bukkit.event.Listener;

public interface EventManager extends Listener {
    List<String> getActiveChecks(ConfigurationCacheStore var1);
}
