package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;

public class NoFallCheck extends MovingCheck {
    public NoFallCheck(NoCheat plugin) {
        super(plugin, "moving.nofall");
    }

    public void check(NoCheatPlayer player, MovingData data, MovingConfig cc) {
        if (player.isCreative()) {
            data.fallDistance = 0.0F;
            data.lastAddedFallDistance = 0.0F;
        } else {
            if (data.fromOnOrInGround) {
                data.fallDistance = 0.0F;
            }

            if (cc.nofallaggressive && data.fromOnOrInGround && data.toOnOrInGround && data.from.y <= data.to.y && player.getPlayer().getFallDistance() > 3.0F) {
                data.fallDistance = player.getPlayer().getFallDistance();
                data.nofallVL = data.nofallVL + data.fallDistance;
                this.incrementStatistics(player, Statistics.Id.MOV_NOFALL, data.fallDistance);
                boolean cancel = this.executeActions(player, cc.nofallActions, data.nofallVL);
                if (cancel) {
                    player.dealFallDamage();
                }

                data.fallDistance = 0.0F;
            }

            if (player.getPlayer().getFallDistance() > data.lastAddedFallDistance) {
                player.getPlayer().setFallDistance(player.getPlayer().getFallDistance() - data.lastAddedFallDistance);
            }

            data.lastAddedFallDistance = 0.0F;
            float difference = data.fallDistance - player.getPlayer().getFallDistance();
            if (difference > 1.0F && data.toOnOrInGround && data.fallDistance > 2.0F) {
                data.nofallVL += difference;
                this.incrementStatistics(player, Statistics.Id.MOV_NOFALL, difference);
                boolean cancel = this.executeActions(player, cc.nofallActions, data.nofallVL);
                if (cancel) {
                    float totalDistance = data.fallDistance + difference * (cc.nofallMultiplier - 1.0F);
                    player.getPlayer().setFallDistance(totalDistance);
                }

                data.fallDistance = 0.0F;
            }

            double oldY = data.from.y;
            double newY = data.to.y;
            if (oldY > newY) {
                float dist = (float)(oldY - newY);
                data.fallDistance += dist;
                if (dist > 1.0F) {
                    data.lastAddedFallDistance = dist;
                    player.getPlayer().setFallDistance(player.getPlayer().getFallDistance() + dist);
                } else {
                    data.lastAddedFallDistance = 0.0F;
                }
            } else {
                data.lastAddedFallDistance = 0.0F;
            }

            data.nofallVL *= 0.95;
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.VIOLATIONS) {
            return String.format(Locale.US, "%d", (int)getData(player).nofallVL);
        } else {
            return wildcard == ParameterName.FALLDISTANCE
                ? String.format(Locale.US, "%.2f", getData(player).fallDistance)
                : super.getParameter(wildcard, player);
        }
    }
}
