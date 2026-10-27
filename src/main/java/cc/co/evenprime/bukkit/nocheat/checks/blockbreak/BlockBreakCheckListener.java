package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockBreakCheckListener implements Listener, EventManager {
    private final NoswingCheck noswingCheck;
    private final ReachCheck reachCheck;
    private final DirectionCheck directionCheck;
    private final FastBreakCheck fastBreakCheck;
    private final NoCheat plugin;

    public BlockBreakCheckListener(NoCheat plugin) {
        this.noswingCheck = new NoswingCheck(plugin);
        this.reachCheck = new ReachCheck(plugin);
        this.directionCheck = new DirectionCheck(plugin);
        this.fastBreakCheck = new FastBreakCheck(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            boolean cancelled = false;
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            BlockBreakConfig cc = BlockBreakCheck.getConfig(player);
            BlockBreakData data = BlockBreakCheck.getData(player);
            data.brokenBlockLocation.set(event.getBlock());
            if (!data.brokenBlockLocation.equals(data.lastDamagedBlock)) {
                data.lastDamagedBlock.reset();
            } else {
                if (cc.noswingCheck && !event.getPlayer().hasPermission("nocheat.checks.blockbreak.noswing")) {
                    cancelled = this.noswingCheck.check(player, data, cc);
                }

                if (!cancelled && cc.reachCheck && !event.getPlayer().hasPermission("nocheat.checks.blockbreak.reach")) {
                    cancelled = this.reachCheck.check(player, data, cc);
                }

                if (!cancelled && cc.directionCheck && !event.getPlayer().hasPermission("nocheat.checks.blockbreak.direction")) {
                    cancelled = this.directionCheck.check(player, data, cc);
                }

                if (cc.fastBreakCheck && !event.getPlayer().hasPermission("nocheat.checks.blockbreak.fastbreak")) {
                    cancelled |= this.fastBreakCheck.check(player, data, cc);
                }

                if (cancelled) {
                    event.setCancelled(cancelled);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockHit(BlockDamageEvent event) {
        if (!event.isCancelled()) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            BlockBreakData data = BlockBreakCheck.getData(player);
            if (event.getInstaBreak()) {
                data.instaBrokenBlockLocation.set(event.getBlock());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            BlockBreakData data = BlockBreakCheck.getData(player);
            data.lastDamagedBlock.set(event.getClickedBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void armSwing(PlayerAnimationEvent event) {
        BlockBreakCheck.getData(this.plugin.getPlayer(event.getPlayer())).armswung = true;
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        BlockBreakConfig bb = BlockBreakCheck.getConfig(cc);
        if (bb.directionCheck) {
            s.add("blockbreak.direction");
        }

        if (bb.reachCheck) {
            s.add("blockbreak.reach");
        }

        if (bb.noswingCheck) {
            s.add("blockbreak.noswing");
        }

        if (bb.fastBreakCheck) {
            s.add("blockbreak.fastbreak");
        }

        return s;
    }
}
