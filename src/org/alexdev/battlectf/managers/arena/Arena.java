package org.alexdev.battlectf.managers.arena;

import org.bukkit.Location;

public class Arena {
    private final String name;
    private final Location firstPoint;
    private final Location secondPoint;

    public Arena(String name, Location firstPoint, Location secondPoint) {
        this.name = name;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
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
}
