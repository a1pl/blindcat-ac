package cc.co.evenprime.bukkit.nocheat.checks;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.Collections;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class WorkaroundsListener implements Listener, EventManager {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            event.setCancelled(false);
            event.setTo(event.getFrom().clone());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void toggleSprint(PlayerToggleSprintEvent event) {
        if (event.isCancelled() && event.isSprinting()) {
            event.setCancelled(false);
        }
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        return Collections.emptyList();
    }
}
