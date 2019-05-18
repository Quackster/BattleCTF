package org.alexdev.battlectf.managers.arena;

import org.alexdev.battlectf.BattleCTF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArenaManager {
    private static ArenaManager instance;

    private String folder;
    private Map<String, Arena> arenaMap;

    public ArenaManager() {
        this.folder = "arenas";
        this.arenaMap = new ConcurrentHashMap<>();
    }

    /**
     * Load arenas from /arenas/ folder
     */
    public void loadArenas() {
        this.arenaMap.clear();

        File directory = new File(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), this.folder);

        if (directory.exists()) {
            if (directory.isFile()) {
                directory.delete();
            }

            directory.mkdir();
        }  else {
            directory.mkdir();
        }

        for (File file : directory.listFiles()) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection arenaConfig = conf.getConfigurationSection("Arena");

            String name = arenaConfig.getString("Name");
            String author = arenaConfig.getString("Author");
            String worldName = arenaConfig.getString("World");

            World world = Bukkit.getServer().getWorld(worldName);

            if (world == null) {
                continue;
            }

            ConfigurationSection regionConfig = conf.getConfigurationSection("Region");

            int minX = regionConfig.getInt("MinX");
            int minY = regionConfig.getInt("MinY");
            int minZ = regionConfig.getInt("MinZ");

            int maxX = regionConfig.getInt("MaxX");
            int maxY = regionConfig.getInt("MaxY");
            int maxZ = regionConfig.getInt("MaxZ");

            Location firstLocation = new Location(world, minX, minY, minZ);
            Location secondLocation = new Location(world, maxX, maxY, maxZ);

            Arena arena = new Arena(name, firstLocation.getWorld().getName(), firstLocation, secondLocation);
            this.arenaMap.put(name, arena);
        }
    }

    /**
     * Create configuration file for the arena.
     *
     * @param name the name
     * @param firstPoint the first point selected
     * @param secondPoint the second point selected
     *
     * @return the arena instance
     */
    public Arena createArena(Player player, String name, Location firstPoint, Location secondPoint) throws IOException {
        Arena arena = new Arena(name, firstPoint.getWorld().getName(), firstPoint, secondPoint);

        File arenaConfig = Paths.get(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), "arenas", name + ".yml").toFile();

        if (!arenaConfig.exists()) {
            arenaConfig.createNewFile();
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(arenaConfig);

        ConfigurationSection arenaDetails = conf.createSection("Arena");
        arenaDetails.set("Name", name);
        arenaDetails.set("World", firstPoint.getWorld().getName());
        arenaDetails.set("Author", player.getName());

        ConfigurationSection regionConfig = conf.createSection("Region");
        regionConfig.set("MinX", firstPoint.getBlockX());
        regionConfig.set("MinY", firstPoint.getBlockY());
        regionConfig.set("MinZ", firstPoint.getBlockZ());
        regionConfig.set("MaxX", secondPoint.getBlockX());
        regionConfig.set("MaxY", secondPoint.getBlockY());
        regionConfig.set("MaxZ", secondPoint.getBlockZ());

        ConfigurationSection teamConfig = conf.createSection("Teams");

        ConfigurationSection firstConfig = teamConfig.createSection("0");
        firstConfig.set("Name", "Red");
        firstConfig.set("Colour", ChatColor.RED.name());
        firstConfig.set("Spawn", "[]");

        ConfigurationSection secondConfig = teamConfig.createSection("1");
        secondConfig.set("Name", "Blue");
        secondConfig.set("Colour", ChatColor.BLUE.name());
        secondConfig.set("Spawn", "[]");

        conf.save(arenaConfig);
        this.arenaMap.put(name, arena);

        return arena;
    }

    /**
     * Get if a location is inside an arena.
     *
     * @param location the location to check
     * @return true, if successful
     */
    public boolean hasArena(Location location) {
        return this.getArenaByLocation(location) != null;
    }

    /**
     * Gets the arena by location specified.
     *
     * @param location the arena
     * @return arena, null if not found
     */
    public Arena getArenaByLocation(Location location) {
        for (Arena arena : this.arenaMap.values()) {
            if (arena.hasLocation(location)) {
                return arena;
            }
        }

        return null;
    }

    /**
     * Get configuration singleton.
     *
     * @return the configuration instance
     */
    public static ArenaManager getInstance() {
        if (instance == null) {
            instance = new ArenaManager();
        }

        return instance;
    }

    /**
     * Get the list of arenas.
     *
     * @return the list of arenas
     */
    public Map<String, Arena> getArenas() {
        return arenaMap;
    }

    /**
     * Get the arenas configuration folder
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Get the arena by name.
     *
     * @param name the name of the arena
     * @return the arena
     */
    public Arena getArena(String name) {
        if (this.arenaMap.containsKey(name)) {
            return this.arenaMap.get(name);
        }

        return null;
    }
}
