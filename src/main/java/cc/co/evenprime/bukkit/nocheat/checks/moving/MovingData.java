package cc.co.evenprime.bukkit.nocheat.checks.moving;

import cc.co.evenprime.bukkit.nocheat.DataItem;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;

public class MovingData implements DataItem {
    public double runflyVL;
    public double nofallVL;
    public double morePacketsVL;
    public int jumpPhase;
    public double lastJumpAmplifier;
    public int onIce;
    public final PreciseLocation runflySetBackPoint = new PreciseLocation();
    public double vertFreedom;
    public double vertVelocity;
    public int vertVelocityCounter;
    public double horizFreedom;
    public int horizVelocityCounter;
    public double horizontalBuffer;
    public int bunnyhopdelay;
    public float fallDistance;
    public float lastAddedFallDistance;
    public long morePacketsLastTime;
    public int packets;
    public int morePacketsBuffer = 50;
    public final PreciseLocation morePacketsSetbackPoint = new PreciseLocation();
    public final PreciseLocation teleportTo = new PreciseLocation();
    public final PreciseLocation from = new PreciseLocation();
    public final PreciseLocation to = new PreciseLocation();
    public boolean fromOnOrInGround;
    public boolean toOnOrInGround;
    public Statistics.Id statisticCategory = Statistics.Id.MOV_RUNNING;
    public double waterwalkVL;
    public int waterWalkTicks;
    public double passableVL;
    public long graceUntil = System.currentTimeMillis() + 1000L;

    public void clearRunFlyData() {
        this.runflySetBackPoint.reset();
        this.jumpPhase = 0;
        this.fallDistance = 0.0F;
        this.lastAddedFallDistance = 0.0F;
        this.bunnyhopdelay = 0;
    }

    public void clearMorePacketsData() {
        this.morePacketsSetbackPoint.reset();
    }
}
