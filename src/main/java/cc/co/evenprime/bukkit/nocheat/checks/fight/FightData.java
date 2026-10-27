package cc.co.evenprime.bukkit.nocheat.checks.fight;

import cc.co.evenprime.bukkit.nocheat.DataItem;

public class FightData implements DataItem {
    public double directionVL;
    public double noswingVL;
    public double reachVL;
    public double speedVL;
    public double velocityVL;
    public double angleVL;
    public double criticalVL;
    public double godModeVL;
    public int godModeBuffer;
    public long godModeLastTime;
    public int godModeLastAge;
    public double selfHitVL;
    public double fastHealVL;
    public long fastHealBuffer;
    public long fastHealRefTime;
    public long lastEntityDamageTime;
    public float lastYaw;
    public float lastPitch;
    public long lastAngleTime;
    public long directionLastViolationTime;
    public long reachLastViolationTime;
    public double reachDistance;
    public boolean armswung = true;
    public long speedTime;
    public int speedAttackCount;
}
