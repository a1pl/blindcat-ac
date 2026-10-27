package cc.co.evenprime.bukkit.nocheat.data;

import org.bukkit.Location;
import org.bukkit.block.Block;

public final class SimpleLocation {
    public int x;
    public int y;
    public int z;

    public SimpleLocation() {
        this.reset();
    }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof SimpleLocation)) {
            return false;
        } else {
            SimpleLocation simpleLocation = (SimpleLocation)object;
            if (!this.isSet() && !simpleLocation.isSet()) {
                return true;
            } else {
                return this.isSet() && simpleLocation.isSet() ? simpleLocation.x == this.x && simpleLocation.y == this.y && simpleLocation.z == this.z : false;
            }
        }
    }

    @Override
    public final int hashCode() {
        return this.x * 1000000 + this.y * 1000 + this.z;
    }

    public final void set(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
    }

    public final void setLocation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public final boolean isSet() {
        return this.x != Integer.MAX_VALUE;
    }

    public final void reset() {
        this.x = Integer.MAX_VALUE;
        this.y = Integer.MAX_VALUE;
        this.z = Integer.MAX_VALUE;
    }
}
