package org.alexdev.battlectf.util;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;

public class Util {
    @NotNull
    public static String describeLocation(Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " in " + location.getWorld().getName();
    }
}
