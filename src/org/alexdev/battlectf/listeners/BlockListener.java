package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
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

        boolean isArenaSelect = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);

        if (isArenaSelect) {
            event.getPlayer().sendMessage(LocaleUtil.getInstance().getFirstPositionSelected(event.getBlock().getLocation()));
            event.setCancelled(true);

            battlePlayer.set(BattleAttribute.SELECT_ARENA_FIRST, event.getBlock().getLocation());
            return;
        }

        boolean isBuildMode = battlePlayer.getOrDefault(BattleAttribute.BUILD, false);

        if (ArenaManager.getInstance().hasArena(event.getBlock().getLocation())) {
            if (!isBuildMode) {
                event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotBreakBlocksInArena());
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(event.getPlayer());

        if (battlePlayer == null) {
            return;
        }

        boolean isArenaSelect = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);

        if (isArenaSelect) {
            event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotBuildArenaSelection());
            event.setCancelled(true);
            return;
        }

        boolean isBuildMode = battlePlayer.getOrDefault(BattleAttribute.BUILD, false);

        if (ArenaManager.getInstance().hasArena(event.getBlockPlaced().getLocation())) {
            if (!isBuildMode) {
                event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotPlaceBlocksInArena());
                event.setCancelled(true);
                return;
            }
        }
    }
}
