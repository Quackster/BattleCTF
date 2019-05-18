package org.alexdev.battlectf.commands;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.alexdev.battlectf.util.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            args[0].equalsIgnoreCase("create")) {

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

            player.sendMessage(ChatColor.YELLOW + "Arena '" + name + "' has been created");
            return true;
        }

        return false;
    }

}
