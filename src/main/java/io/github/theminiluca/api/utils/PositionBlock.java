package io.github.theminiluca.utils;

import io.github.theminiluca.roin.war.plugin.RoinWar;
import io.github.theminiluca.sql.SQLObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PositionBlock implements ConfigurationSerializable, Serializable, Cloneable {

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    private PositionBlock(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private PositionBlock(Block block) {
        this(Objects.requireNonNull(block.getLocation().getWorld()).getName(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
    }

    public void place(Material material) {
        Bukkit.getWorld(getWorld()).setType(toLocation(), material);
    }
    public static PositionBlock blockPos(World world, int x, int y, int z) {
        return new PositionBlock(world.getName(), x, y, z);
    }
    public static PositionBlock blockPos(String worldName, int x, int y, int z) {
        return new PositionBlock(worldName, x, y, z);
    }
    public static PositionBlock blockPos(Block block) {
        return new PositionBlock(block);
    }
    public static PositionBlock blockPos(Location loc) {
        return new PositionBlock(Objects.requireNonNull(loc.getWorld()).getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionBlock position = (PositionBlock) o;

        if (x != position.x) return false;
        if (y != position.y) return false;
        if (z != position.z) return false;
        return Objects.equals(world, position.world);
    }

    @Override
    public int hashCode() {
        int result = world != null ? world.hashCode() : 0;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "world=" + world +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    private final String world;
    private int x;
    public int y;
    private int z;

    public PositionBlock setX(int x) {
        PositionBlock block = clone();
        block.x = x;
        return block;
    }

    public PositionBlock setY(int y) {
        PositionBlock block = clone();
        block.y = y;
        return block;
    }

    public PositionBlock setZ(int z) {
        PositionBlock block = clone();
        block.z = z;
        return block;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("world", world);
        hash.put("x", x);
        hash.put("y", y);
        hash.put("z", z);
        return hash;
    }
    @NotNull
    public static PositionBlock deserialize(Map<String, Object> hash) {
        return new PositionBlock((String) hash.get("world"),
                (int) hash.get("x"),
                (int) hash.get("y"),
                (int) hash.get("z"));
    }

    @Override
    public PositionBlock clone() {
        return PositionBlock.blockPos(this.toLocation());
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
