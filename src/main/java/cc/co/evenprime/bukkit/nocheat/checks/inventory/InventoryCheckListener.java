package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryCheckListener implements Listener, EventManager {
    private final DropCheck dropCheck;
    private final InstantBowCheck instantBowCheck;
    private final InstantEatCheck instantEatCheck;
    private final NoCheat plugin;

    public InventoryCheckListener(NoCheat plugin) {
        this.dropCheck = new DropCheck(plugin);
        this.instantBowCheck = new InstantBowCheck(plugin);
        this.instantEatCheck = new InstantEatCheck(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    protected void handlePlayerDropItemEvent(PlayerDropItemEvent event) {
        if (!event.isCancelled() && !event.getPlayer().isDead()) {
            boolean cancelled = false;
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            InventoryConfig cc = InventoryCheck.getConfig(player);
            InventoryData data = InventoryCheck.getData(player);
            if (cc.dropCheck && !player.hasPermission("nocheat.checks.inventory.drop")) {
                cancelled = this.dropCheck.check(player, data, cc);
            }

            if (cancelled) {
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void interact(PlayerInteractEvent event) {
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            InventoryData data = InventoryCheck.getData(player);
            if (event.getItem().getType() == Material.BOW) {
                data.lastBowInteractTime = System.currentTimeMillis();
            } else if (CheckUtil.isFood(event.getItem())) {
                data.foodMaterial = event.getItem().getType();
                data.lastEatInteractTime = System.currentTimeMillis();
            } else {
                data.lastBowInteractTime = 0L;
                data.lastEatInteractTime = 0L;
                data.foodMaterial = null;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void foodchanged(FoodLevelChangeEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player) {
            NoCheatPlayer player = this.plugin.getPlayer((Player)event.getEntity());
            InventoryConfig cc = InventoryCheck.getConfig(player);
            InventoryData data = InventoryCheck.getData(player);
            if (cc.eatCheck && !player.hasPermission("nocheat.checks.inventory.instanteat")) {
                boolean cancelled = this.instantEatCheck.check(player, event, data, cc);
                event.setCancelled(cancelled);
            }

            data.foodMaterial = null;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void bowfired(EntityShootBowEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player) {
            NoCheatPlayer player = this.plugin.getPlayer((Player)event.getEntity());
            InventoryConfig cc = InventoryCheck.getConfig(player);
            if (cc.bowCheck && !player.hasPermission("nocheat.checks.inventory.instantbow")) {
                InventoryData data = InventoryCheck.getData(player);
                boolean cancelled = this.instantBowCheck.check(player, event, data, cc);
                event.setCancelled(cancelled);
            }
        }
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        InventoryConfig i = InventoryCheck.getConfig(cc);
        if (i.dropCheck) {
            s.add("inventory.dropCheck");
        }

        if (i.bowCheck) {
            s.add("inventory.instantbow");
        }

        if (i.eatCheck) {
            s.add("inventory.instanteat");
        }

        return s;
    }
}
