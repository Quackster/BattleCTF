package org.alexdev.battlectf.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;


public class LocaleUtil {
    private static LocaleUtil instance;

    public String getFirstPositionSelected(Location location) {
        return ChatColor.AQUA + "First position for arena selection at " + Util.describeLocation(location);
    }

    public String getSecondPositionSelected(Location location) {
        return ChatColor.AQUA + "Second position for arena selection at " + Util.describeLocation(location);
    }

    public String getErrorOccurred() {
        return ChatColor.RED + "Error occurred";
    }

    public String getCannotBreakBlocksInArena() {
        return ChatColor.RED + "You cannot break blocks in the arena";
    }

    public String getCannotPlaceBlocksInArena() {
        return ChatColor.RED + "You cannot place blocks in the arena";
    }

    public String getCannotBuildArenaSelection() {
        return ChatColor.RED + "Can't build while arena coordinate selection is on";
    }

    public String getBuildModeToggle(boolean mode) {
        return ChatColor.GREEN + "Build mode in arena is set to " + (mode ? "true" : "false");
    }

    public String getSelectingToggle(boolean mode) {
        return ChatColor.GREEN + "Selecting arena is set to " + (mode ? "true" : "false");
    }

    public String getNoNameProvided() {
        return ChatColor.RED + "There was no name provided for the arena";
    }

    public String getNoFirstPosition() {
        return ChatColor.RED + "You did not select the first position for the arena";
    }

    public String getNoSecondPosition() {
        return ChatColor.RED + "You did not select the second position for the arena";
    }

    public String getArenaCreated(String name) {
        return ChatColor.YELLOW + "Arena '" + name + "' has been created";
    }

    public String getArenaReset(String name) {
        return ChatColor.YELLOW + "Arena '" + name + "' has been reset";
    }

    public String getArenaSaved(String name) {
        return ChatColor.GREEN + "Arena '" + name + "' has been saved";
    }

    /**
     * Get configuration singleton.
     *
     * @return the configuration instance
     */
    public static LocaleUtil getInstance() {
        if (instance == null) {
            instance = new LocaleUtil();
        }

        return instance;
    }
}
