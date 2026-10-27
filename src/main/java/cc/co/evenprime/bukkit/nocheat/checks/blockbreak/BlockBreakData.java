package cc.co.evenprime.bukkit.nocheat.checks.blockbreak;

import cc.co.evenprime.bukkit.nocheat.DataItem;
import cc.co.evenprime.bukkit.nocheat.data.SimpleLocation;

public class BlockBreakData implements DataItem {
    public double reachVL = 0.0;
    public double directionVL = 0.0;
    public double noswingVL = 0.0;
    public double fastBreakVL = 0.0;
    public long lastBreakTime = 0L;
    public long directionLastViolationTime = 0L;
    public final SimpleLocation instaBrokenBlockLocation = new SimpleLocation();
    public final SimpleLocation brokenBlockLocation = new SimpleLocation();
    public final SimpleLocation lastDamagedBlock = new SimpleLocation();
    public boolean armswung = true;
    public double reachDistance;
}
