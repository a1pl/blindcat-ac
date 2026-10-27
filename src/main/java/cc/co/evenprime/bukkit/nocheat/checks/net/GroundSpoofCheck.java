package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.CheckUtil;
import cc.co.evenprime.bukkit.nocheat.checks.moving.MovingCheck;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GroundSpoofCheck extends NetCheck {
    public GroundSpoofCheck(NoCheat plugin) {
        super(plugin, "net.groundspoof");
    }

    public boolean check(NoCheatPlayer player, NetData data, NetConfig cc, boolean packetOnGround) {
        if (System.currentTimeMillis() < MovingCheck.getData(player).graceUntil) {
            data.groundSpoofAirTicks = 0;
            return false;
        } else {
            Player bukkitPlayer = player.getPlayer();
            World world = bukkitPlayer.getWorld();
            PreciseLocation loc = new PreciseLocation();
            loc.set(bukkitPlayer.getLocation());
            int locationType = CheckUtil.evaluateLocation(world, loc);
            boolean reallyGrounded = CheckUtil.isOnGround(locationType) || CheckUtil.isInGround(locationType);
            if (reallyGrounded) {
                data.groundSpoofAirTicks = 0;
                data.groundSpoofVL *= 0.8;
                return false;
            } else {
                data.groundSpoofAirTicks++;
                if (packetOnGround && data.groundSpoofAirTicks > cc.groundSpoofAirTicks) {
                    double over = data.groundSpoofAirTicks - cc.groundSpoofAirTicks;
                    data.groundSpoofVL += over;
                    this.incrementStatistics(player, Statistics.Id.NET_GROUNDSPOOF, over);
                    return this.executeActions(player, cc.groundSpoofActions, data.groundSpoofVL);
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        return wildcard == ParameterName.VIOLATIONS ? String.format(Locale.US, "%d", (int)getData(player).groundSpoofVL) : super.getParameter(wildcard, player);
    }
}
