package org.alexdev.battlectf.managers.arena;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionIntersection;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Arena {
    private String name;
    private String world;
    private Location firstPoint;
    private Location secondPoint;

    private Map<ArenaFlags, Boolean> flagsMap;

    public Arena(String name, String world, Location firstPoint, Location secondPoint) {
        this.name = name;
        this.world = world;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.flagsMap = new ConcurrentHashMap<ArenaFlags, Boolean>() {{
            put(ArenaFlags.ALLOW_BLOCK_BREAKING, false);
            put(ArenaFlags.ALLOW_BLOCK_PLACING, false);
            put(ArenaFlags.ALLOW_ANIMAL_SPAWNING, false);
            put(ArenaFlags.ALLOW_MOB_SPAWNING, false);
            put(ArenaFlags.ALLOW_EXPLOSIONS, true);
            put(ArenaFlags.ALLOW_LAVA_FLOW, true);
            put(ArenaFlags.ALLOW_WATER_FLOW, true);
            put(ArenaFlags.ALLOW_LEAF_DECAY, true);
            put(ArenaFlags.ALLOW_FIRE_SPREAD, true);
            put(ArenaFlags.ALLOW_GRASS_SPREAD, true);
            put(ArenaFlags.ALLOW_MUSHROOM_SPREAD, false);
            put(ArenaFlags.ALLOW_VINE_SPREAD, false);

        }};
    }

    /**
     * Get if the arena has a flag.
     *
     * @param arenaFlags the flag to check
     * @return true, if successful
     */
    public boolean hasFlag(ArenaFlags arenaFlags) {
        if (this.flagsMap.containsKey(arenaFlags)) {
            return this.flagsMap.get(arenaFlags);
        }

        return false;
    }

    /**
     * Return if a block is inside a region.
     *
     * @param location the location to check
     * @return true, if it is
     */
    public boolean hasLocation(Location location) {
        return this.getCuboidRegion().contains(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    /**
     * Get if the block is an arena border block.
     *
     * @param location the location to check
     * @return true, if successful
     */
    public boolean isBorder(Location location) {
        CuboidRegion cuboidRegion = this.getCuboidRegion();
        return cuboidRegion.getFaces().contains(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    /**
     * Get the cuboid region instance of the arena.
     *
     * @return the cuboid region
     */
    public CuboidRegion getCuboidRegion() {
        CuboidRegion region = new CuboidRegion(new BukkitWorld(this.getWorld()),
                BlockVector3.at(this.firstPoint.getBlockX(), this.firstPoint.getBlockY(), this.firstPoint.getBlockZ()),
                BlockVector3.at(this.secondPoint.getBlockX(), this.secondPoint.getBlockY(), this.secondPoint.getBlockZ())
        );

        return region;
    }

    /**
     * Get the world this region is located in.
     *
     * @return the world
     */
    public World getWorld() {
        return Bukkit.getServer().getWorld(this.world);
    }

    /**
     * Get the arena name.
     *
     * @return the name of the arena
     */
    public String getName() {
        return name;
    }

    /**
     * Get the first point of the arena.
     *
     * @return the first point
     */
    public Location getFirstPoint() {
        return firstPoint;
    }

    /**
     * Get the second point of the arena.
     *
     * @return the second point
     */
    public Location getSecondPoint() {
        return secondPoint;
    }

    /**
     * Get the flags for the arena.
     *
     * @return the flags
     */
    public Map<ArenaFlags, Boolean> getFlags() {
        return flagsMap;
    }
}
