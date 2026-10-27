package cc.co.evenprime.bukkit.nocheat.data;

import org.bukkit.Location;

public final class PreciseLocation {
    public double x;
    public double y;
    public double z;

    public PreciseLocation() {
        this.reset();
    }

    public final void set(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public final void set(PreciseLocation location) {
        this.x = location.x;
        this.y = location.y;
        this.z = location.z;
    }

    public final boolean isSet() {
        return this.x != Double.MAX_VALUE;
    }

    public final void reset() {
        this.x = Double.MAX_VALUE;
        this.y = Double.MAX_VALUE;
        this.z = Double.MAX_VALUE;
    }

    public final boolean equals(Location location) {
        return location.getX() == this.x && location.getY() == this.y && location.getZ() == this.z;
    }
}
