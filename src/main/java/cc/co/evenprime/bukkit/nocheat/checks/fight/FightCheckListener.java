package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class FightCheckListener implements Listener, EventManager {
    private final SpeedCheck speedCheck;
    private final NoswingCheck noswingCheck;
    private final ReachCheck reachCheck;
    private final DirectionCheck directionCheck;
    private final VelocityCheck velocityCheck;
    private final AngleCheck angleCheck;
    private final CriticalCheck criticalCheck;
    private final GodModeCheck godModeCheck;
    private final SelfHitCheck selfHitCheck;
    private final FastHealCheck fastHealCheck;
    private final NoCheat plugin;

    public FightCheckListener(NoCheat plugin) {
        this.speedCheck = new SpeedCheck(plugin);
        this.noswingCheck = new NoswingCheck(plugin);
        this.reachCheck = new ReachCheck(plugin);
        this.directionCheck = new DirectionCheck(plugin);
        this.velocityCheck = new VelocityCheck(plugin);
        this.angleCheck = new AngleCheck(plugin);
        this.criticalCheck = new CriticalCheck(plugin);
        this.godModeCheck = new GodModeCheck(plugin);
        this.selfHitCheck = new SelfHitCheck(plugin);
        this.fastHealCheck = new FastHealCheck(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void attack(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getCause() == DamageCause.ENTITY_ATTACK && event.getDamager() instanceof Player) {
            Player attacker = (Player)event.getDamager();
            Entity target = event.getEntity();
            if (!attacker.isDead() && attacker.getWorld().equals(target.getWorld())) {
                if (target instanceof Player) {
                    this.velocityCheck.damaged(FightCheck.getData(this.plugin.getPlayer((Player)target)));
                }

                NoCheatPlayer player = this.plugin.getPlayer(attacker);
                FightConfig cc = FightCheck.getConfig(player);
                FightData data = FightCheck.getData(player);
                boolean cancelled = false;
                if (cc.selfHitCheck && !player.hasPermission("nocheat.checks.fight.selfhit")) {
                    cancelled = this.selfHitCheck.check(player, target, data, cc);
                }

                if (cc.speedCheck && !player.hasPermission("nocheat.checks.fight.speed")) {
                    cancelled |= this.speedCheck.check(player, data, cc);
                }

                if (cc.noswingCheck && !player.hasPermission("nocheat.checks.fight.noswing")) {
                    cancelled |= this.noswingCheck.check(player, data, cc);
                }

                if (!cancelled && cc.reachCheck && !player.hasPermission("nocheat.checks.fight.reach")) {
                    cancelled = this.reachCheck.check(player, target, data, cc);
                }

                if (!cancelled && cc.directionCheck && !player.hasPermission("nocheat.checks.fight.direction")) {
                    cancelled = this.directionCheck.check(player, target, data, cc);
                }

                if (cc.angleCheck && !player.hasPermission("nocheat.checks.fight.angle")) {
                    cancelled |= this.angleCheck.check(player, data, cc);
                }

                if (cc.criticalCheck && !player.hasPermission("nocheat.checks.fight.critical")) {
                    cancelled |= this.criticalCheck.check(player, data, cc);
                }

                if (cancelled) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damaged(EntityDamageEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player) {
            NoCheatPlayer player = this.plugin.getPlayer((Player)event.getEntity());
            FightConfig cc = FightCheck.getConfig(player);
            if (cc.godModeCheck && !player.hasPermission("nocheat.checks.fight.godmode")) {
                this.godModeCheck.check(player, FightCheck.getData(player), cc);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void regainHealth(EntityRegainHealthEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player && event.getRegainReason() == RegainReason.SATIATED) {
            NoCheatPlayer player = this.plugin.getPlayer((Player)event.getEntity());
            FightConfig cc = FightCheck.getConfig(player);
            if (cc.fastHealCheck && !player.hasPermission("nocheat.checks.fight.fastheal")) {
                if (this.fastHealCheck.check(player, FightCheck.getData(player), cc)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void velocity(PlayerVelocityEvent event) {
        if (!event.isCancelled()) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            FightConfig cc = FightCheck.getConfig(player);
            if (cc.velocityCheck && !player.hasPermission("nocheat.checks.fight.velocity")) {
                FightData data = FightCheck.getData(player);
                Vector v = event.getVelocity();
                this.velocityCheck.applied(player, data, cc, v.getX(), v.getZ());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void armSwing(PlayerAnimationEvent event) {
        FightData data = FightCheck.getData(this.plugin.getPlayer(event.getPlayer()));
        data.armswung = true;
        this.speedCheck.swing(data);
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        FightConfig f = FightCheck.getConfig(cc);
        if (f.speedCheck) {
            s.add("fight.speed");
        }

        if (f.noswingCheck) {
            s.add("fight.noswing");
        }

        if (f.reachCheck) {
            s.add("fight.reach");
        }

        if (f.directionCheck) {
            s.add("fight.direction");
        }

        if (f.velocityCheck) {
            s.add("fight.velocity");
        }

        if (f.angleCheck) {
            s.add("fight.angle");
        }

        if (f.criticalCheck) {
            s.add("fight.critical");
        }

        if (f.godModeCheck) {
            s.add("fight.godmode");
        }

        if (f.selfHitCheck) {
            s.add("fight.selfhit");
        }

        if (f.fastHealCheck) {
            s.add("fight.fastheal");
        }

        return s;
    }
}
