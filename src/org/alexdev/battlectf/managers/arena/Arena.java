package org.alexdev.battlectf.managers.arena;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.managers.team.TeamColour;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Arena {
    private String name;
    private String world;
    private Location firstPoint;
    private Location secondPoint;

    private Map<ArenaFlags, Boolean> flagsMap;
    private List<ArenaTeam> teamList;
    private List<ItemStack> spawnItems;

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

        this.teamList = new CopyOnWriteArrayList<>();
    }

    /**
     * Join team handler.
     *
     * @param player the player
     * @param teamName the team name
     */
    public void joinTeam(Player player, String teamName) {
        ArenaTeam team = null;

        if (teamName != null) {
            team = this.getTeamByName(teamName);

        } else {
            // Find team with lowest team members to add to
            List<ArenaTeam> sortedTeamList = new ArrayList<>(this.teamList);
            sortedTeamList.sort(Comparator.comparingInt(t -> t.getTeamPlayers().size()));

            if (sortedTeamList.size() > 0) {
                team = sortedTeamList.get(0);
            }
        }

        if (team != null) {
            team.join(player);
        } else {
            System.out.println("Could not join team?");
        }
    }

    /**
     * Reapply the spawn items on the player.
     *
     * @param player the player to refresh the inventory for
     */
    public void refreshInventory(Player player) {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(player);

        if (battlePlayer == null) {
            return;
        }

        Color teamColour = TeamColour.get(battlePlayer.getTeam().getChatColor());

        if (teamColour == null) {
            return;
        }

        player.getInventory().clear();
        player.sendMessage(ChatColor.ITALIC + "Refreshing inventory...");

        ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
        lam.setColor(teamColour);
        lhelmet.setItemMeta(lam);
        player.getInventory().setHelmet(lhelmet);

        lhelmet = new ItemStack(Material.LEATHER_BOOTS, 1);
        lam = (LeatherArmorMeta)lhelmet.getItemMeta();
        lam.setColor(teamColour);
        lhelmet.setItemMeta(lam);
        player.getInventory().setBoots(lhelmet);

        lhelmet = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        lam = (LeatherArmorMeta)lhelmet.getItemMeta();
        lam.setColor(teamColour);
        lhelmet.setItemMeta(lam);
        player.getInventory().setChestplate(lhelmet);

        lhelmet = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        lam = (LeatherArmorMeta)lhelmet.getItemMeta();
        lam.setColor(teamColour);
        lhelmet.setItemMeta(lam);
        player.getInventory().setLeggings(lhelmet);

        for (ItemStack itemStack : this.spawnItems) {
            player.getInventory().addItem(itemStack.clone());
        }

        player.updateInventory();
    }

    /**
     * Refresh the arena teams.
     */
    public void refreshTeams() {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(this.getConfigFile());
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

        this.shuffleTeams();
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
            if (entity.getType() != EntityType.PLAYER) {
                entity.remove();
            }
        }
    }

    public void save(Player player) {
        World world = Bukkit.getWorld(this.world);

        if (world == null) {
            player.sendMessage(LocaleUtil.getInstance().getWorldNotFound(this.world));
            return;
        }

        if (SchematicManager.save(player, world, this.name, this.getFirstPoint(), this.getSecondPoint())) {
            player.sendMessage(LocaleUtil.getInstance().getArenaSaved(this.name));
        } else{
            player.sendMessage(LocaleUtil.getInstance().getErrorOccurred());
        }
    }

    /**
     * Get all players who are playing in the arena.
     *
     * @return the players
     */
    public List<BattlePlayer> getAllPlayers() {
        List<BattlePlayer> battlePlayers = new ArrayList<>();

        for (ArenaTeam arenaTeam : this.teamList) {
            battlePlayers.addAll(arenaTeam.getTeamPlayers());
        }

        return battlePlayers;
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

    /**
     * Shuffle teams.
     */
    public void shuffleTeams() {
        List<ArenaTeam> tempTeams = new ArrayList<ArenaTeam>(this.teamList);
        Collections.shuffle(tempTeams);
        this.teamList = tempTeams;

    }

    /**
     * Get the respawn items.
     *
     * @return the respawn items.
     */
    public List<ItemStack> getSpawnItems() {
        return spawnItems;
    }

    /**
     * Set the spawn items.
     *
     * @param spawnItems the spawn items
     */
    public void setSpawnItems(List<ItemStack> spawnItems) {
        this.spawnItems = spawnItems;
    }
}
