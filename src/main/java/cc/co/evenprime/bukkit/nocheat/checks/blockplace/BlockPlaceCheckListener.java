package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceCheckListener implements Listener, EventManager {
    private final ReachCheck reachCheck;
    private final DirectionCheck directionCheck;
    private final FastPlaceCheck fastPlaceCheck;
    private final NoCheat plugin;

    public BlockPlaceCheckListener(NoCheat plugin) {
        this.plugin = plugin;
        this.reachCheck = new ReachCheck(plugin);
        this.directionCheck = new DirectionCheck(plugin);
        this.fastPlaceCheck = new FastPlaceCheck(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    protected void handleBlockPlaceEvent(BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getBlock() != null && event.getBlockAgainst() != null) {
            boolean cancelled = false;
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            BlockPlaceConfig cc = BlockPlaceCheck.getConfig(player);
            BlockPlaceData data = BlockPlaceCheck.getData(player);
            data.blockPlaced.set(event.getBlock());
            data.blockPlacedAgainst.set(event.getBlockAgainst());
            if (cc.reachCheck && !player.hasPermission("nocheat.checks.blockplace.reach")) {
                cancelled = this.reachCheck.check(player, data, cc);
            }

            if (!cancelled && cc.directionCheck && !player.hasPermission("nocheat.checks.blockplace.direction")) {
                cancelled = this.directionCheck.check(player, data, cc);
            }

            if (cc.fastPlaceCheck && !player.hasPermission("nocheat.checks.blockplace.fastplace")) {
                cancelled |= this.fastPlaceCheck.check(player, data, cc);
            }

            if (cancelled) {
                event.setCancelled(cancelled);
            }
        }
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        BlockPlaceConfig bp = BlockPlaceCheck.getConfig(cc);
        if (bp.reachCheck) {
            s.add("blockplace.reach");
        }

        if (bp.directionCheck) {
            s.add("blockplace.direction");
        }

        if (bp.fastPlaceCheck) {
            s.add("blockplace.fastplace");
        }

        return s;
    }
}
