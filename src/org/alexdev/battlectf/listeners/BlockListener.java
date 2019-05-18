package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaFlags;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.alexdev.battlectf.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

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
            Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getBlock().getLocation());

            if (!isBuildMode) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_BLOCK_BREAKING)) {
                    event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotBreakBlocksInArena());
                    event.setCancelled(true);
                    return;
                }

                if (arena.isBorder(event.getBlock().getLocation())) {
                    event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotBreakBlocksInArena());
                    event.setCancelled(true);
                    return;
                }

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
            Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getBlockPlaced().getLocation());

            if (!isBuildMode) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_BLOCK_PLACING)) {
                    event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotPlaceBlocksInArena());
                    event.setCancelled(true);
                    return;
                }

                if (arena.isBorder(event.getBlock().getLocation())) {
                    event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotPlaceBlocksInArena());
                    event.setCancelled(true);
                    return;
                }

                event.getPlayer().sendMessage(LocaleUtil.getInstance().getCannotPlaceBlocksInArena());
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent event){
        boolean explodingBlockInArena = ArenaManager.getInstance().hasArena(event.getBlock().getLocation());

        if (explodingBlockInArena) {
            for (Iterator<Block> it = event.blockList().iterator(); it.hasNext();) {
                Block block = it.next();
                Arena arena = ArenaManager.getInstance().getArenaByLocation(block.getLocation());

                if (arena == null) {
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
