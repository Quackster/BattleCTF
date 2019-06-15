package org.alexdev.battlectf.managers.arena;

import org.alexdev.battlectf.BattleCTF;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

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
    @SuppressWarnings("deprecation")
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
            List<String> flags = (List<String>) arenaConfig.getList("Flags");

            for (String flag : flags) {
                String[] detail = flag.split(Pattern.quote(":"));
                arena.getFlags().put(ArenaFlags.valueOf(detail[0]), detail[1].equalsIgnoreCase("true"));
            }

            List<ItemStack> items = new ArrayList<ItemStack>();

            try {
                for (String line : conf.getStringList("Items")) {
                    System.out.println("ededede: " + line);

                    String[] str = null;

                    if (line.contains("|")) {
                        str = line.split(Pattern.quote("|"))[0].split(",");

                    } else {
                        str = line.split(",");
                    }

                    ItemStack stack = new ItemStack(Material.valueOf(str[0]));
                    stack.setAmount(Integer.parseInt(str[1]));

                    line = line.replace(str[0] + "," + str[0] + "|", "");

                    if (line.contains("|")) {
                        for (String e : line.replace(line.split("\\|")[0] + "|", "").split("\\|")) {
                            ItemMeta meta = stack.getItemMeta();

                            int enchantmentLevel = Integer.parseInt(e.split(",")[1]);
                            meta.addEnchant(Enchantment.getByName(e.split(",")[0]), enchantmentLevel, true);

                            stack.setItemMeta(meta);
                        }
                    }

                    items.add(stack);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            arena.refreshTeams();
            arena.setSpawnItems(items);

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
        File arenaConfig = arena.getConfigFile();

        if (!arenaConfig.exists()) {
            arenaConfig.createNewFile();
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(arenaConfig);

        ConfigurationSection arenaDetails = conf.createSection("Arena");
        arenaDetails.set("Name", name);
        arenaDetails.set("World", firstPoint.getWorld().getName());
        arenaDetails.set("Author", player.getName());

        List<String> flags = new ArrayList<>();

        for (Map.Entry<ArenaFlags, Boolean> kvp : arena.getFlags().entrySet()) {
            flags.add(kvp.getKey().name() + ":" + (kvp.getValue() ? "true" : "false"));
        }

        arenaDetails.set("Flags", flags);

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
        firstConfig.set("Spawn", "null");

        ConfigurationSection secondConfig = teamConfig.createSection("1");
        secondConfig.set("Name", "Blue");
        secondConfig.set("Colour", ChatColor.BLUE.name());
        secondConfig.set("Spawn", "null");

        List<String> items = new ArrayList<>();
        items.add("IRON_SWORD,1");
        items.add("BOW,1|ARROW_INFINITE,1");
        items.add("IRON_AXE,1");
        items.add("IRON_PICKAXE,1");
        items.add("OAK_LOG,64");
        items.add("GLASS,64");
        items.add("COOKED_BEEF,12");
        items.add("GOLDEN_APPLE,3");
        items.add("ARROW,1");
        items.add("DIAMOND_BLOCK,4");
        items.add("IRON_BLOCK,1");
        conf.set("Items", items);

        conf.save(arenaConfig);
        arena.refreshTeams();

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
