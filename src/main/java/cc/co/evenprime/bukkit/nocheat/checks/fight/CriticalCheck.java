package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class CriticalCheck extends FightCheck {
    public CriticalCheck(NoCheat plugin) {
        super(plugin, "fight.critical");
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        Player bukkitPlayer = player.getPlayer();
        float fallDistance = bukkitPlayer.getFallDistance();
        if (!(fallDistance <= 0.0F) && !bukkitPlayer.isInsideVehicle() && !bukkitPlayer.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            PreciseLocation loc = new PreciseLocation();
            loc.set(bukkitPlayer.getLocation());
            int type = CheckUtil.evaluateLocation(bukkitPlayer.getWorld(), loc);
            if (!CheckUtil.isOnGround(type) && !CheckUtil.isInGround(type) && !CheckUtil.isLiquid(type)) {
                double velocityY = Math.abs(bukkitPlayer.getVelocity().getY());
                if (fallDistance >= cc.criticalFallDistance && velocityY >= cc.criticalVelocity) {
                    return false;
                }

                double deltaFallDistance = Math.max(0.0, (cc.criticalFallDistance - fallDistance) / cc.criticalFallDistance);
                double deltaVelocity = Math.max(0.0, (cc.criticalVelocity - velocityY) / cc.criticalVelocity);
                double delta = Math.max(deltaFallDistance, deltaVelocity);
                data.criticalVL += delta;
                this.incrementStatistics(player, Statistics.Id.FI_CRITICAL, delta);
                ImprobableCheck.report(player, 0.5);
                return this.executeActions(player, cc.criticalActions, data.criticalVL);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).criticalVL) : super.getParameter(wildcard, player);
    }
}
