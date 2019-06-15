package org.alexdev.battlectf.managers.players;

import org.alexdev.battlectf.managers.arena.ArenaTeam;
import org.alexdev.battlectf.util.attributes.BattleAttributable;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BattlePlayer extends BattleAttributable {
    private final Player player;

    private ArenaTeam team;

    private String survivalInventory;
    private Location survivalLocation;
    private GameMode gameMode;
    private int level;

    private float exp;
    private int totalExperience;

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
     * Set the exp of the player before they joined to the team
     * @return the experience
     */
    public float getExp() {
        return exp;
    }

    /**
     * Get the last location of the player before they joined to the team
     * @param exp the exp
     */
    public void setExp(float exp) {
        this.exp = exp;
    }

    /**
     * Get the total exp of the player before they joined to the team
     * @return the total exp
     */
    public int getTotalExperience() {
        return totalExperience;
    }

    /**
     * Set the total exp of the player before they joined to the team
     * @param totalExperience the total experience
     */
    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    /**
     * Get the exp to level before game start
     * @return the exp to level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the exp to level before game start
     * @param level the exp to level
     */
    public void setLevel(int level) {
        this.level = level;
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

    /**
     * Get the game mode before they joined the game
     * @return the game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Set the game mode before they joined the game
     * @param gameMode the game mode
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
