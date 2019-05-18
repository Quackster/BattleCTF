package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SaveCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "/ctf reset [name]");
            return true;
        }

        String name = args[1];

        if (name.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There was no name provided for the arena");
            return true;
        }

        Arena arena = ArenaManager.getInstance().getArena(name);

        Location first = arena.getFirstPoint();
        Location second = arena.getSecondPoint();

        if (SchematicManager.save(player, name, first, second)) {
            try {
                ArenaManager.getInstance().createArena(player, name, first, second);
                player.sendMessage(ChatColor.GREEN + "Arena '" + name + "' has been saved");
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Error occurred");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Error occurred");
        }

        return true;

    }
}
