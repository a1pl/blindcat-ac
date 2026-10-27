package cc.co.evenprime.bukkit.nocheat.checks;

import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.data.PreciseLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CheckUtil {
    private static final double magic = 0.45;
    private static final double magic2 = 0.55;
    private static final int NONSOLID = 1;
    private static final int SOLID = 2;
    private static final int LIQUID = 5;
    private static final int INGROUND = 128;
    private static final int ONGROUND = 256;

    public static final double directionCheck(
        NoCheatPlayer player, double targetX, double targetY, double targetZ, double targetWidth, double targetHeight, double precision
    ) {
        Location eyes = player.getPlayer().getEyeLocation();
        double factor = Math.sqrt(Math.pow(eyes.getX() - targetX, 2.0) + Math.pow(eyes.getY() - targetY, 2.0) + Math.pow(eyes.getZ() - targetZ, 2.0));
        Vector direction = eyes.getDirection();
        double x = targetX - eyes.getX();
        double y = targetY - eyes.getY();
        double z = targetZ - eyes.getZ();
        double xPrediction = factor * direction.getX();
        double yPrediction = factor * direction.getY();
        double zPrediction = factor * direction.getZ();
        double off = 0.0;
        off += Math.max(Math.abs(x - xPrediction) - (targetWidth / 2.0 + precision), 0.0);
        off += Math.max(Math.abs(z - zPrediction) - (targetWidth / 2.0 + precision), 0.0);
        off += Math.max(Math.abs(y - yPrediction) - (targetHeight / 2.0 + precision), 0.0);
        if (off > 1.0) {
            off = Math.sqrt(off);
        }

        return off;
    }

    public static final double reachCheck(NoCheatPlayer player, double targetX, double targetY, double targetZ, double limit) {
        Location eyes = player.getPlayer().getEyeLocation();
        double distance = Math.sqrt(Math.pow(eyes.getX() - targetX, 2.0) + Math.pow(eyes.getY() - targetY, 2.0) + Math.pow(eyes.getZ() - targetZ, 2.0));
        return Math.max(distance - limit, 0.0);
    }

    public static final int evaluateLocation(World world, PreciseLocation location) {
        int lowerX = lowerBorder(location.x);
        int upperX = upperBorder(location.x);
        int Y = (int)location.y;
        int lowerZ = lowerBorder(location.z);
        int upperZ = upperBorder(location.z);
        int result = 0;
        result |= evaluateSimpleLocation(world, lowerX, Y, lowerZ);
        result |= evaluateSimpleLocation(world, upperX, Y, lowerZ);
        result |= evaluateSimpleLocation(world, upperX, Y, upperZ);
        result |= evaluateSimpleLocation(world, lowerX, Y, upperZ);
        if (!isInGround(result)
            && world.getBlockAt(Location.locToBlock(location.x), Location.locToBlock(location.y), Location.locToBlock(location.z)).getType().isSolid()) {
            result |= 128;
        }

        return result;
    }

    private static final int evaluateSimpleLocation(World world, int x, int y, int z) {
        Block top = world.getBlockAt(x, y + 1, z);
        Block base = world.getBlockAt(x, y, z);
        Block below = world.getBlockAt(x, y - 1, z);
        int type = 0;
        if (below.getType() == Material.FENCE && base.getType() != Material.FENCE && top.getType().isSolid()) {
            type = 128;
        } else if (below.getType() != Material.FENCE && !base.getType().isSolid() && world.getBlockAt(x, y - 2, z).getType() == Material.FENCE) {
            type = 256;
        } else if (!top.getType().isSolid()) {
            if (below.getType().isSolid() && !base.getType().isSolid()) {
                type = 256;
            } else if (base.getType() == Material.LADDER || top.getType() == Material.LADDER) {
                type = 256;
            } else if (base.getType().isSolid()) {
                type = 128;
            }
        }

        if (base.isLiquid() || top.isLiquid()) {
            type |= 133;
        }

        return type;
    }

    public static final boolean isSolid(int value) {
        return (value & 2) == 2;
    }

    public static final boolean isLiquid(int value) {
        return (value & 5) == 5;
    }

    public static final boolean isOnGround(int fromType) {
        return (fromType & 256) == 256;
    }

    public static final boolean isInGround(int fromType) {
        return (fromType & 128) == 128;
    }

    private static final int lowerBorder(double d1) {
        double floor = Math.floor(d1);
        return floor + 0.45 <= d1 ? (int)floor : (int)(floor - 1.0);
    }

    private static final int upperBorder(double d1) {
        double floor = Math.floor(d1);
        return floor + 0.55 < d1 ? (int)(floor + 1.0) : (int)floor;
    }

    public static boolean isFood(ItemStack item) {
        return item == null ? false : item.getType().isEdible();
    }

    public static final boolean isInsideBlock(World world, PreciseLocation location) {
        double halfWidth = 0.3;

        for (double dx : new double[]{-0.3, 0.3}) {
            for (double dz : new double[]{-0.3, 0.3}) {
                for (double dy : new double[]{0.1, 1.5}) {
                    Block block = world.getBlockAt(
                        Location.locToBlock(location.x + dx), Location.locToBlock(location.y + dy), Location.locToBlock(location.z + dz)
                    );
                    if (block.getType().isSolid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
