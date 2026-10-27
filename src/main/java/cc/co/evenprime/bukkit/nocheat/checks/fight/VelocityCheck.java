package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;

public class VelocityCheck extends FightCheck {
    private static final long COMBAT_WINDOW_MS = 150L;
    private static final long CHECK_DELAY_TICKS = 4L;

    public VelocityCheck(NoCheat plugin) {
        super(plugin, "fight.velocity");
    }

    public void damaged(FightData data) {
        data.lastEntityDamageTime = System.currentTimeMillis();
    }

    public void applied(final NoCheatPlayer player, final FightData data, final FightConfig cc, double vx, double vz) {
        if (System.currentTimeMillis() - data.lastEntityDamageTime <= 150L) {
            final double expected = Math.sqrt(vx * vx + vz * vz);
            if (!(expected < 0.05)) {
                Location from = player.getPlayer().getLocation();
                final double fromX = from.getX();
                final double fromZ = from.getZ();
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (!player.isDead()) {
                            Location to = player.getPlayer().getLocation();
                            double dx = to.getX() - fromX;
                            double dz = to.getZ() - fromZ;
                            double traveled = Math.sqrt(dx * dx + dz * dz);
                            double required = expected * cc.velocityThreshold;
                            if (traveled < required) {
                                double off = required - traveled;
                                data.velocityVL += off * 10.0;
                                VelocityCheck.this.incrementStatistics(player, Statistics.Id.FI_VELOCITY, off);
                                ImprobableCheck.report(player, 2.0);
                                VelocityCheck.this.executeActions(player, cc.velocityActions, data.velocityVL);
                            } else {
                                data.velocityVL *= 0.7;
                            }
                        }
                    }
                }, 4L);
            }
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).velocityVL) : super.getParameter(wildcard, player);
    }
}
