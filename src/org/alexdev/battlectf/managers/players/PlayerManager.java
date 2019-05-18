package org.alexdev.battlectf.managers.players;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;
    private Map<String, BattlePlayer> playerMap;

    public PlayerManager() {
        this.playerMap = new ConcurrentHashMap<>();
    }

    public void addPlayer(Player player) {
        this.playerMap.put(player.getUniqueId().toString(), new BattlePlayer(player));
    }

    public BattlePlayer getPlayer(Player player) {
        if (!this.playerMap.containsKey(player.getUniqueId().toString())) {
            return null;
        }

        return this.playerMap.get(player.getUniqueId().toString());
    }


    public boolean hasPlayer(Player player) {
        return this.playerMap.containsKey(player.getUniqueId().toString());
    }

    public void removePlayer(Player player) {
        this.playerMap.remove(player.getUniqueId().toString());
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
}
