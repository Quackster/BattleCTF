package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaFlags;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityListener implements Listener {
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!ArenaManager.getInstance().hasArena(event.getLocation())) {
            return;
        }

        Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getLocation());

        if (event.getEntity() instanceof Animals) {
            if (!arena.hasFlag(ArenaFlags.ALLOW_ANIMAL_SPAWNING)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getEntity() instanceof Monster) {
            if (!arena.hasFlag(ArenaFlags.ALLOW_MOB_SPAWNING)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
