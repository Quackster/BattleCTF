package org.alexdev.battlectf.listeners;

import org.alexdev.battlectf.managers.players.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!PlayerManager.getInstance().hasPlayer(event.getPlayer())) {
            PlayerManager.getInstance().addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (PlayerManager.getInstance().hasPlayer(event.getPlayer())) {
            PlayerManager.getInstance().removePlayer(event.getPlayer());
        }
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {

    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

    }


    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {


    }

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent e) {

    }


    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {

    }
}
