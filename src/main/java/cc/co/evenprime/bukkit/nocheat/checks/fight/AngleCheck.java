package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Location;

public class AngleCheck extends FightCheck {
    private static final long CONSECUTIVE_WINDOW_MS = 100L;

    public AngleCheck(NoCheat plugin) {
        super(plugin, "fight.angle");
    }

    public boolean check(NoCheatPlayer player, FightData data, FightConfig cc) {
        Location eyes = player.getPlayer().getEyeLocation();
        float yaw = eyes.getYaw();
        float pitch = eyes.getPitch();
        long time = System.currentTimeMillis();
        boolean cancel = false;
        if (data.lastAngleTime != 0L && time - data.lastAngleTime < 100L) {
            double delta = Math.abs(normalize(yaw - data.lastYaw)) + Math.abs(pitch - data.lastPitch);
            double off = delta - cc.angleLimit;
            if (off > 0.0) {
                data.angleVL += off;
                this.incrementStatistics(player, Statistics.Id.FI_ANGLE, off);
                ImprobableCheck.report(player, 1.5);
                cancel = this.executeActions(player, cc.angleActions, data.angleVL);
            } else {
                data.angleVL *= 0.7;
            }
        }

        data.lastYaw = yaw;
        data.lastPitch = pitch;
        data.lastAngleTime = time;
        return cancel;
    }

    private static float normalize(float angle) {
        angle %= 360.0F;
        if (angle >= 180.0F) {
            angle -= 360.0F;
        } else if (angle < -180.0F) {
            angle += 360.0F;
        }

        return angle;
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).angleVL) : super.getParameter(wildcard, player);
    }
}
