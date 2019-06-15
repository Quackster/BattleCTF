package org.alexdev.battlectf.managers.arena;

import org.alexdev.battlectf.BattleCTF;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.List;

public class ArenaTeam {
    private int position;
    private Arena arena;
    private String name;
    private ChatColor chatColor;
    private Location spawn;

    public ArenaTeam(Arena arena, int position, String name, String colour, String spawnCoordinates) {
        this.position = position;
        this.arena = arena;
        this.name = name;
        this.chatColor = ChatColor.valueOf(colour);

        String[] spawnValues = spawnCoordinates.split(",");

        try {
            this.spawn = new Location(arena.getWorld(),
                    Double.valueOf(String.valueOf(spawnValues[0])),
                    Double.valueOf(String.valueOf(spawnValues[1])),
                    Double.valueOf(String.valueOf(spawnValues[2])),
                    Float.valueOf(String.valueOf(spawnValues[3])),
                    Float.valueOf(String.valueOf(spawnValues[4])));
        } catch (Exception ex) {
            BattleCTF.getInstance().getLogger().warning("The team " + this.name + " has no spawn point!");
        }
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
}
