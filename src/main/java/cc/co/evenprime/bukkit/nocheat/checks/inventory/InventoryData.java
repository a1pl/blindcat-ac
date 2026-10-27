package cc.co.evenprime.bukkit.nocheat.checks.inventory;

import cc.co.evenprime.bukkit.nocheat.DataItem;
import org.bukkit.Material;

public class InventoryData implements DataItem {
    public int dropVL;
    public int instantBowVL;
    public double instantEatVL;
    public long dropLastTime;
    public int dropCount;
    public long lastBowInteractTime;
    public long lastEatInteractTime;
    public Material foodMaterial;
}
