package org.alexdev.battlectf.managers.arena;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Arena {
    private String name;
    private String world;
    private Location firstPoint;
    private Location secondPoint;

    public Arena(String name, String world, Location firstPoint, Location secondPoint) {
        this.name = name;
        this.world = world;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
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
}
