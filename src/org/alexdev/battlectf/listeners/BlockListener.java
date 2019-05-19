package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaFlags;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.alexdev.battlectf.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
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
    public void onBlockExplode(BlockExplodeEvent event){
        Arena source = ArenaManager.getInstance().getArenaByLocation(event.getBlock().getLocation());
        boolean explodingBlockInArena = ArenaManager.getInstance().hasArena(event.getBlock().getLocation());

        if (explodingBlockInArena) {
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

        for (Iterator<Block> it = event.blockList().iterator(); it.hasNext();) {
            Block block = it.next();

            Arena arena = ArenaManager.getInstance().getArenaByLocation(block.getLocation());

            if (arena != null && !arena.hasFlag(ArenaFlags.ALLOW_EXPLOSIONS)) {
                it.remove();
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event){
        Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getBlock().getLocation());
        Arena destination = ArenaManager.getInstance().getArenaByLocation(event.getToBlock().getLocation());

        if (arena != destination) {
            event.setCancelled(true);
            return;
        }

        if (arena != null) {
            if (event.getBlock().getType() == Material.LAVA) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_LAVA_FLOW)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (event.getBlock().getType() == Material.WATER) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_WATER_FLOW)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getBlock().getLocation());
        Arena source = ArenaManager.getInstance().getArenaByLocation(event.getSource().getLocation());

        if (arena != source) {
            event.setCancelled(true);
            return;
        }

        if (arena != null) {
            if (event.getSource().getType() == Material.FIRE) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_FIRE_SPREAD)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (event.getSource().getType() == Material.GRASS_BLOCK) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_GRASS_SPREAD)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (event.getSource().getType() == Material.VINE) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_VINE_SPREAD)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (event.getSource().getType() == Material.BROWN_MUSHROOM || event.getSource().getType() == Material.RED_MUSHROOM) {
                if (!arena.hasFlag(ArenaFlags.ALLOW_MUSHROOM_SPREAD)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingBlock() == null) {
            return;
        }

        Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getIgnitingBlock().getLocation());

        if (arena != null) {
            if (!arena.hasFlag(ArenaFlags.ALLOW_FIRE_IGNITE)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.getIgnitingBlock() == null) {
            return;
        }

        Arena arena = ArenaManager.getInstance().getArenaByLocation(event.getIgnitingBlock().getLocation());

        if (arena != null) {
            if (!arena.hasFlag(ArenaFlags.ALLOW_FIRE_BURN)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
