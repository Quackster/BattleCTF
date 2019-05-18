package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.alexdev.battlectf.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            return;
        }

        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(event.getPlayer());

        if (battlePlayer == null) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            boolean isArenaSelect = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);

            if (isArenaSelect) {
                event.getPlayer().sendMessage(LocaleUtil.getInstance().getSecondPositionSelected(event.getClickedBlock().getLocation()));
                event.setCancelled(true);

                battlePlayer.set(BattleAttribute.SELECT_ARENA_SECOND, event.getClickedBlock().getLocation());
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

    }
}