package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaFlags;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

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

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event){
        Arena source = ArenaManager.getInstance().getArenaByLocation(event.getLocation());
        boolean explodingEntityInArena = source != null;

        if (explodingEntityInArena) {
            for (Iterator<Block> it = event.blockList().iterator(); it.hasNext();) {
                Block block = it.next();
                Arena arena = ArenaManager.getInstance().getArenaByLocation(block.getLocation());

                if (arena == null || source != arena) {
                    it.remove();
                } else if (arena.isBorder(block.getLocation())) {
                    it.remove();
                }
            }
        } else {
            for (Iterator<Block> it = event.blockList().iterator(); it.hasNext();) {
                Block block = it.next();
                Arena arena = ArenaManager.getInstance().getArenaByLocation(block.getLocation());

                if (arena != null) {
                    it.remove();
                }
            }
        }
    }
}
