package org.alexdev.battlectf.managers.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;
    private Map<String, BattlePlayer> playerMap;

    public PlayerManager() {
        this.playerMap = new ConcurrentHashMap<>();
    }

    /**
     * Adds the player to the map
     *
     * @param player the player
     */
    public void addPlayer(Player player) {
        this.playerMap.put(player.getUniqueId().toString(), new BattlePlayer(player));
    }

    /**
     * Get the battle player instance by player instance
     * @param player the player
     * @return the instance, if successful
     */
    public BattlePlayer getPlayer(Player player) {
        if (!this.playerMap.containsKey(player.getUniqueId().toString())) {
            return null;
        }

        return this.playerMap.get(player.getUniqueId().toString());
    }

    /**
     * Check if the server contains the player
     * @param player the player
     * @return true, if successful
     */
    public boolean hasPlayer(Player player) {
        return this.playerMap.containsKey(player.getUniqueId().toString());
    }

    /**
     * Remove the player from the map
     *
     * @param player the player
     */
    public void removePlayer(Player player) {
        this.playerMap.remove(player.getUniqueId().toString());
    }

    /**
     * Get the entire player map.
     *
     * @return the player map
     */
    public Map<String, BattlePlayer> getPlayers() {
        return playerMap;
    }

    /**
     * Get the singleton instance.
     *
     * @return the singleton
     */
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    /**
     * Reloads all the players on the server
     */
    public void reloadPlayers() {
        PlayerManager.getInstance().getPlayers().clear();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PlayerManager.getInstance().addPlayer(player);
        }

    }
}
