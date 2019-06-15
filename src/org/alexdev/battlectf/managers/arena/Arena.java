package org.alexdev.battlectf.managers.arena;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Arena {
    private String name;
    private String world;
    private Location firstPoint;
    private Location secondPoint;

    private Map<ArenaFlags, Boolean> flagsMap;
    private List<ArenaTeam> teamList;

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
            put(ArenaFlags.ALLOW_FIRE_IGNITE, true);
            put(ArenaFlags.ALLOW_FIRE_BURN, true);
            put(ArenaFlags.ALLOW_GRASS_SPREAD, true);
            put(ArenaFlags.ALLOW_MUSHROOM_SPREAD, false);
            put(ArenaFlags.ALLOW_VINE_SPREAD, false);

        }};

        this.teamList = new ArrayList<>();
    }

    /**
     * Refresh the arena teams.
     *
     * @param conf the configuration instance
     */
    public void refreshTeams(FileConfiguration conf) {
        this.teamList.clear();

        for (int i = 0; i < 16; i++) {
            ConfigurationSection configurationSection = conf.getConfigurationSection("Teams." + i);

            if (configurationSection == null) {
                continue;
            }

            this.teamList.add(new ArenaTeam(this,
                    i,
                    configurationSection.getString("Name"),
                    configurationSection.getString("Colour"),
                    configurationSection.getString("Spawn")));
        }
    }

    /**
     * Resets the arena blocks.
     */
    public void reset() {
        World world = Bukkit.getWorld(this.world);

        if (world == null) {
            return;
        }

        Clipboard clipboard = SchematicManager.load(this.name);
        SchematicManager.paste(world, clipboard);

        for (Entity entity : world.getNearbyEntities(this.getBoundingBox())) {
            if (!(entity.getType() == EntityType.ITEM_FRAME && entity instanceof Animals && entity instanceof Monster && entity.getType() == EntityType.DROPPED_ITEM)) {
                continue;
            }

            entity.remove();
        }
    }

    public void save(Player player) {
        World world = Bukkit.getWorld(this.world);

        if (world == null) {
            player.sendMessage(LocaleUtil.getInstance().getWorldNotFound(this.world));
            return;
        }

        if (!SchematicManager.save(player, world, this.name, this.getFirstPoint(), this.getSecondPoint())) {
            return;
        }

        try {
            ArenaManager.getInstance().createArena(player, name, this.getFirstPoint(), this.getSecondPoint());
            player.sendMessage(LocaleUtil.getInstance().getArenaSaved(name));
        } catch (IOException e) {
            player.sendMessage(LocaleUtil.getInstance().getErrorOccurred());
        }
    }

    /**
     * Get team by name.
     *
     * @param name the name of the team
     */
    public ArenaTeam getTeamByName(String name) {
        for (ArenaTeam arenaTeam : this.teamList) {
            if (arenaTeam.getName().equalsIgnoreCase(name)) {
                return arenaTeam;
            }
        }

        return null;
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
        return new CuboidRegion(new BukkitWorld(this.getWorld()),
                BlockVector3.at(this.firstPoint.getBlockX(), this.firstPoint.getBlockY(), this.firstPoint.getBlockZ()),
                BlockVector3.at(this.secondPoint.getBlockX(), this.secondPoint.getBlockY(), this.secondPoint.getBlockZ())
        );
    }

    /**
     * Get the bounding box for this arena.
     *
     * @return the bounding box
     */
    private BoundingBox getBoundingBox() {
        CuboidRegion region = this.getCuboidRegion();

        return new BoundingBox(
                region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ(),
                region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
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

    /**
     * Gets the path to the arena config file.
     *
     * @return the arena config
     */
    public File getConfigFile() {
        return Paths.get(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), "arenas", name + ".yml").toFile();
    }

    /**
     * Get team list
     * @return the team list
     */
    public List<ArenaTeam> getTeamList() {
        return teamList;
    }
}
