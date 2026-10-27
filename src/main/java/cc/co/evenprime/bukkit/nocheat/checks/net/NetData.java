package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.DataItem;

public class NetData implements DataItem {
    public double flyingBuffer = 44.0;
    public long flyingLastTime;
    public double flyingVL;
    public long lastKeepAliveTime;
    public int keepAliveBurstCount;
    public double keepAliveVL;
    public long attackWindowStart;
    public int attackWindowCount;
    public double attackVL;
    public int groundSpoofAirTicks;
    public double groundSpoofVL;
}
