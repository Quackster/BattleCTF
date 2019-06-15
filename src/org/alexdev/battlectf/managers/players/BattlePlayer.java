package org.alexdev.battlectf.managers.players;

import org.alexdev.battlectf.managers.arena.ArenaTeam;
import org.alexdev.battlectf.util.attributes.BattleAttributable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BattlePlayer extends BattleAttributable {
    private final Player player;

    private ArenaTeam team;

    private String survivalInventory;
    private Location survivalLocation;

    public BattlePlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the inventory string saved after joining a game.
     *
     * @return the inventory string
     */
    public String getSurvivalInventory() {
        return survivalInventory;
    }

    /**
     * Set the inventory string after joining the game.
     *
     * @param survivalInventory the inventory
     */
    public void setSurvivalInventory(String survivalInventory) {
        this.survivalInventory = survivalInventory;
    }

    /**
     * Get the last location of the player before they joined to the team
     * @return the location
     */
    public Location getSurvivalLocation() {
        return survivalLocation;
    }

    /**
     * Set the last location of the player before they joined to the team
     * @param survivalLocation the location
     */
    public void setSurvivalLocation(Location survivalLocation) {
        this.survivalLocation = survivalLocation;
    }

    /**
     * Get arena team.
     *
     * @return the team
     */
    public ArenaTeam getTeam() {
        return team;
    }

    /**
     * Set arena team.
     *
     * @param team the team
     */
    public void setTeam(ArenaTeam team) {
        this.team = team;
    }

    /**
     * Get the player for this battleplayer.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
