package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.ConfigItem;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;

public class NetConfig implements ConfigItem {
    public final boolean flyingFrequencyCheck;
    public final double flyingFrequencyPPS;
    public final ActionList flyingFrequencyActions;
    public final boolean keepAliveFrequencyCheck;
    public final long keepAliveFrequencyWindowMillis;
    public final int keepAliveFrequencyBurstLimit;
    public final ActionList keepAliveFrequencyActions;
    public final boolean attackFrequencyCheck;
    public final int attackFrequencyLimit;
    public final ActionList attackFrequencyActions;
    public final boolean groundSpoofCheck;
    public final int groundSpoofAirTicks;
    public final ActionList groundSpoofActions;

    public NetConfig(NoCheatConfiguration data) {
        this.flyingFrequencyCheck = data.getBoolean("checks.net.flyingfrequency.active");
        this.flyingFrequencyPPS = Math.max(1, data.getInt("checks.net.flyingfrequency.packetspersecond"));
        this.flyingFrequencyActions = data.getActionList("checks.net.flyingfrequency.actions", "nocheat.checks.net.flyingfrequency");
        this.keepAliveFrequencyCheck = data.getBoolean("checks.net.keepalivefrequency.active");
        this.keepAliveFrequencyWindowMillis = data.getInt("checks.net.keepalivefrequency.windowmillis");
        this.keepAliveFrequencyBurstLimit = data.getInt("checks.net.keepalivefrequency.burstlimit");
        this.keepAliveFrequencyActions = data.getActionList("checks.net.keepalivefrequency.actions", "nocheat.checks.net.keepalivefrequency");
        this.attackFrequencyCheck = data.getBoolean("checks.net.attackfrequency.active");
        this.attackFrequencyLimit = data.getInt("checks.net.attackfrequency.limit");
        this.attackFrequencyActions = data.getActionList("checks.net.attackfrequency.actions", "nocheat.checks.net.attackfrequency");
        this.groundSpoofCheck = data.getBoolean("checks.net.groundspoof.active");
        this.groundSpoofAirTicks = data.getInt("checks.net.groundspoof.airticks");
        this.groundSpoofActions = data.getActionList("checks.net.groundspoof.actions", "nocheat.checks.net.groundspoof");
    }
}
