package org.alexdev.battlectf.managers.arena;

import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.InventoryBase64;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaTeam {
    private int position;
    private Arena arena;
    private String name;
    private ChatColor chatColor;
    private Location spawn;
    private List<BattlePlayer> teamPlayers;

    public ArenaTeam(Arena arena, int position, String name, String colour, String spawnCoordinates) {
        this.position = position;
        this.arena = arena;
        this.name = name;
        this.chatColor = ChatColor.valueOf(colour);
        this.teamPlayers = new ArrayList<>();

        String[] spawnValues = spawnCoordinates.split(",");

        try {
            this.spawn = new Location(arena.getWorld(),
                    Double.valueOf(String.valueOf(spawnValues[0])),
                    Double.valueOf(String.valueOf(spawnValues[1])),
                    Double.valueOf(String.valueOf(spawnValues[2])),
                    Float.valueOf(String.valueOf(spawnValues[3])),
                    Float.valueOf(String.valueOf(spawnValues[4])));
        } catch (Exception ex) {
            BattleCTF.getInstance().getLogger().warning("The team " + this.name + " for the arena " + arena.getName() + " has no spawn point!");
        }

        this.resetTeam();
    }

    /**
     * Method for resetting team
     */
    private void resetTeam() {
        this.teamPlayers.clear();


    }

    /**
     * Handler for joining game.
     *
     * @param player the player
     */
    public void join(Player player) {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(player);

        if (battlePlayer == null) {
            return;
        }

        battlePlayer.setTeam(this);
        battlePlayer.setSurvivalLocation(player.getLocation().clone());
        battlePlayer.setSurvivalInventory(InventoryBase64.toBase64(player.getInventory()));
        battlePlayer.setGameMode(player.getGameMode());
        battlePlayer.setExp(player.getExp());
        battlePlayer.setTotalExperience(player.getTotalExperience());
        battlePlayer.setLevel(player.getLevel());

        this.respawnPlayer(player);
        player.teleport(this.spawn);
    }

    /**
     * Respawn player handler
     */
    private void respawnPlayer(Player player) {
        player.setFallDistance(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setSaturation(20);
        player.setExhaustion(0);
        player.setTotalExperience(0);
        player.setExp(0.0f);
        player.setLevel(-99999);
        player.setFoodLevel(20);

        this.arena.refreshInventory(player);
    }

    /**
     * Leave team handler
     * @param player the player to leave
     */
    public void leaveTeam(Player player) throws IOException {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(player);

        if (battlePlayer == null) {
            return;
        }

        player.getInventory().clear();
        player.teleport(battlePlayer.getSurvivalLocation());
        player.setFallDistance(0);
        player.setGameMode(battlePlayer.getGameMode());
        player.setExp(battlePlayer.getExp());
        player.setTotalExperience(battlePlayer.getTotalExperience());
        player.setLevel(battlePlayer.getLevel());

        Inventory inventory = InventoryBase64.fromBase64(battlePlayer.getSurvivalInventory());
        player.getInventory().setContents(inventory.getContents());

        battlePlayer.setTeam(null);
    }

    /**
     * Save the team details.
     */
    public void save() {
        String prefix = "Teams." + this.position + ".";
        FileConfiguration conf = YamlConfiguration.loadConfiguration(arena.getConfigFile());

        try {
            conf.set(prefix + "Name", this.name);
            conf.set(prefix + "Colour", this.chatColor.name());
            conf.set(prefix + "Spawn", this.spawn.getX() + "," + this.spawn.getY() + "," + this.spawn.getZ() + "," + this.spawn.getYaw() + "," + this.spawn.getPitch());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            conf.save(this.arena.getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the position the team is in from the configuration file.
     *
     * @return the team position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Get the arena that the team is in.
     *
     * @return the arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Get the name of the team.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the chat colour of the team.
     *
     * @return the chat colour
     */
    public ChatColor getChatColor() {
        return chatColor;
    }

    /**
     * Get the spawn of the team
     *
     * @return the spawn
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * Set the team spawn.
     *
     * @param spawn the spawn.
     */
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    /**
     * Get the team players.
     *
     * @return the team players
     */
    public List<BattlePlayer> getTeamPlayers() {
        return teamPlayers;
    }

}
