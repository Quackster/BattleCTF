package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.BattleAttribute;
import org.alexdev.battlectf.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(event.getPlayer());

        if (battlePlayer == null) {
            return;
        }

        boolean isArenaSelect = battlePlayer.get(BattleAttribute.SELECT_ARENA);

        if (isArenaSelect) {
            event.getPlayer().sendMessage(ChatColor.AQUA + "First position for arena selection at " + Util.describeLocation(event.getBlock().getLocation()));
            event.setCancelled(true);

            battlePlayer.set(BattleAttribute.SELECT_ARENA_FIRST, event.getBlock().getLocation());
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(event.getPlayer());

        if (battlePlayer == null) {
            return;
        }

        boolean isArenaSelect = battlePlayer.get(BattleAttribute.SELECT_ARENA);

        if (isArenaSelect) {
            event.getPlayer().sendMessage(ChatColor.RED + "Can't build while arena coordinate selection is on");
            event.setCancelled(true);
            return;
        }
    }
}
