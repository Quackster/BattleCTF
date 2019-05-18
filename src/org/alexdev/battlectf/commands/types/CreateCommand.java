package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CreateCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "/ctf create [name]");
            return true;
        }

        String name = args[1];

        if (name.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There was no name provided for the arena");
            return true;
        }

        Location first = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA_FIRST, null);
        Location second = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA_SECOND, null);

        if (first == null) {
            player.sendMessage(ChatColor.RED + "You did not select the first position for the arena");
            return true;
        }

        if (second == null) {
            player.sendMessage(ChatColor.RED + "You did not select the second position for the arena");
            return true;
        }

        if (SchematicManager.save(player, name, first, second)) {
            //SchematicManager.paste(player, SchematicManager.load(player, name), region.getMaximumPoint().getBlockX(), region.getMaximumPoint().getBlockY(), region.getMaximumPoint().getBlockZ());
            try {
                ArenaManager.getInstance().createArena(player, name, first, second);
                player.sendMessage(ChatColor.YELLOW + "Arena '" + name + "' has been created");
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Error occurred");
            }
        }

        return true;
    }
}
