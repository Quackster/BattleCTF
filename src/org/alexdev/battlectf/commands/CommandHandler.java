package org.alexdev.battlectf.commands;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.arena.SchematicManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        BattlePlayer battlePlayer = PlayerManager.getInstance().getPlayer(player);

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Not enough arguments provided");
            return true;
        }


        if (args[0].equalsIgnoreCase("select") ||
            args[0].equalsIgnoreCase("create") ||
            args[0].equalsIgnoreCase("save")) {

            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to perform this action");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("select")) {
            boolean isBuilder = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);
            battlePlayer.set(BattleAttribute.SELECT_ARENA, !isBuilder);

            player.sendMessage(ChatColor.GREEN + "Selecting arena is set to " + (!isBuilder ? "true" : "false"));
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
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

        if (args[0].equalsIgnoreCase("reset")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "/ctf reset [name]");
                return true;
            }

            String name = args[1];

            if (name.isEmpty()) {
                player.sendMessage(ChatColor.RED + "There was no name provided for the arena");
                return true;
            }

            Clipboard clipboard = SchematicManager.load(player, name);
            SchematicManager.paste(player, clipboard);

            player.sendMessage(ChatColor.YELLOW + "Arena '" + name + "' has been reset");
            return true;
        }

        if (args[0].equalsIgnoreCase("save")) {
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
            }

            return true;
        }

        return false;
    }

}
