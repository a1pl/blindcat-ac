package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class MovingCheckListener implements Listener, EventManager {
    private final MorePacketsCheck morePacketsCheck;
    private final FlyingCheck flyingCheck;
    private final RunningCheck runningCheck;
    private final WaterWalkCheck waterWalkCheck;
    private final PassableCheck passableCheck;
    private final NoCheat plugin;

    public MovingCheckListener(NoCheat plugin) {
        this.flyingCheck = new FlyingCheck(plugin);
        this.runningCheck = new RunningCheck(plugin);
        this.morePacketsCheck = new MorePacketsCheck(plugin);
        this.waterWalkCheck = new WaterWalkCheck(plugin);
        this.passableCheck = new PassableCheck(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            MovingConfig config = MovingCheck.getConfig(player);
            if (!config.allowFlying
                && config.runflyCheck
                && !event.getPlayer().hasPermission("nocheat.checks.moving.flying")
                && !event.getPlayer().hasPermission("nocheat.checks.moving.runfly")) {
                MovingData data = MovingCheck.getData(player);
                Block block = event.getBlockPlaced();
                if (block != null && data.runflySetBackPoint.isSet()) {
                    Location l = player.getPlayer().getLocation();
                    int playerX = l.getBlockX();
                    int playerY = l.getBlockY();
                    int playerZ = l.getBlockZ();
                    int blockY = block.getY();
                    if (Math.abs(playerX - block.getX()) <= 1
                        && Math.abs(playerZ - block.getZ()) <= 1
                        && playerY - blockY >= 0
                        && playerY - blockY <= 2
                        && (block.isLiquid() || block.getType().isSolid())
                        && blockY + 1 >= data.runflySetBackPoint.y) {
                        data.runflySetBackPoint.y = blockY + 1;
                        data.jumpPhase = 0;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void teleport(PlayerTeleportEvent event) {
        NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
        MovingData data = MovingCheck.getData(player);
        if (data.teleportTo.isSet() && data.teleportTo.equals(event.getTo())) {
            event.setCancelled(false);
        } else {
            data.clearMorePacketsData();
        }

        data.teleportTo.reset();
        data.clearRunFlyData();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void worldChange(PlayerChangedWorldEvent event) {
        MovingData data = MovingCheck.getData(this.plugin.getPlayer(event.getPlayer()));
        data.teleportTo.reset();
        data.clearRunFlyData();
        data.clearMorePacketsData();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void portal(PlayerPortalEvent event) {
        MovingData data = MovingCheck.getData(this.plugin.getPlayer(event.getPlayer()));
        data.clearMorePacketsData();
        data.clearRunFlyData();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void respawn(PlayerRespawnEvent event) {
        MovingData data = MovingCheck.getData(this.plugin.getPlayer(event.getPlayer()));
        data.clearMorePacketsData();
        data.clearRunFlyData();
        data.graceUntil = System.currentTimeMillis() + 1000L;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void move(PlayerMoveEvent event) {
        if (!event.isCancelled() && !event.getPlayer().isInsideVehicle()) {
            if (event.getFrom().getWorld().equals(event.getTo().getWorld()) && !(event.getFrom().distanceSquared(event.getTo()) > 400.0)) {
                NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
                MovingConfig cc = MovingCheck.getConfig(player);
                MovingData data = MovingCheck.getData(player);
                boolean canFly = event.getPlayer().getAllowFlight();
                this.tickVelocities(data);
                data.from.set(event.getFrom());
                Location to = event.getTo();
                data.to.set(to);
                PreciseLocation newTo = null;
                boolean exempt = System.currentTimeMillis() < data.graceUntil;
                if (cc.passableCheck
                    && System.currentTimeMillis() >= data.graceUntil
                    && event.getPlayer().getGameMode() != GameMode.SPECTATOR
                    && !player.hasPermission("nocheat.checks.moving.passable")) {
                    newTo = this.passableCheck.check(player, data, cc);
                }

                if (!cc.runflyCheck || canFly || exempt || player.hasPermission("nocheat.checks.moving.runfly")) {
                    data.clearRunFlyData();
                } else if (!cc.allowFlying && !player.hasPermission("nocheat.checks.moving.flying")) {
                    newTo = this.runningCheck.check(player, data, cc);
                } else {
                    newTo = this.flyingCheck.check(player, data, cc);
                }

                if (!cc.morePacketsCheck || canFly || exempt || player.hasPermission("nocheat.checks.moving.morepackets")) {
                    data.clearMorePacketsData();
                } else if (newTo == null) {
                    newTo = this.morePacketsCheck.check(player, data, cc);
                }

                if (cc.waterwalkCheck && !canFly && !exempt && !player.hasPermission("nocheat.checks.moving.waterwalk")) {
                    this.waterWalkCheck.check(player, data, cc);
                } else {
                    data.waterWalkTicks = 0;
                }

                if (newTo != null) {
                    event.setTo(new Location(player.getPlayer().getWorld(), newTo.x, newTo.y, newTo.z, to.getYaw(), to.getPitch()));
                    data.teleportTo.set(newTo);
                }
            }
        }
    }

    private void tickVelocities(MovingData data) {
        if (data.horizVelocityCounter > 0) {
            data.horizVelocityCounter--;
        } else if (data.horizFreedom > 0.001) {
            data.horizFreedom *= 0.9;
        }

        if (data.vertVelocity <= 0.1) {
            data.vertVelocityCounter--;
        }

        if (data.vertVelocityCounter > 0) {
            data.vertFreedom = data.vertFreedom + data.vertVelocity;
            data.vertVelocity *= 0.9;
        } else if (data.vertFreedom > 0.001) {
            data.vertFreedom *= 0.93;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void velocity(PlayerVelocityEvent event) {
        if (!event.isCancelled()) {
            MovingData data = MovingCheck.getData(this.plugin.getPlayer(event.getPlayer()));
            Vector v = event.getVelocity();
            double newVal = v.getY();
            if (newVal >= 0.0) {
                data.vertVelocity += newVal;
                data.vertFreedom = data.vertFreedom + data.vertVelocity;
            }

            data.vertVelocityCounter = 50;
            newVal = Math.sqrt(Math.pow(v.getX(), 2.0) + Math.pow(v.getZ(), 2.0));
            if (newVal > 0.0) {
                data.horizFreedom += newVal;
                data.horizVelocityCounter = 30;
            }
        }
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        MovingConfig m = MovingCheck.getConfig(cc);
        if (m.runflyCheck) {
            if (!m.allowFlying) {
                s.add("moving.runfly");
                if (m.sneakingCheck) {
                    s.add("moving.sneaking");
                }

                if (m.nofallCheck) {
                    s.add("moving.nofall");
                }
            } else {
                s.add("moving.flying");
            }
        }

        if (m.morePacketsCheck) {
            s.add("moving.morepackets");
        }

        if (m.waterwalkCheck) {
            s.add("moving.waterwalk");
        }

        if (m.passableCheck) {
            s.add("moving.passable");
        }

        return s;
    }
}
