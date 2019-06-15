package org.alexdev.battlectf.commands;

import org.alexdev.battlectf.commands.types.*;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.bukkit.ChatColor;
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
            args[0].equalsIgnoreCase("create") ||
            args[0].equalsIgnoreCase("build") ||
            args[0].equalsIgnoreCase("save") ||
            args[0].equalsIgnoreCase("reload") ||
            args[0].equalsIgnoreCase("set")) {

            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to perform this action");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("select")) {
            return SelectCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("create")) {
            return CreateCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("reset")) {
            return ResetCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("save")) {
            return SaveCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("build")) {
            return BuildCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            return ReloadCommand.onCommand(battlePlayer, player, args);
        }

        if (args[0].equalsIgnoreCase("set")) {
            return SetCommand.onCommand(battlePlayer, player, args);
        }

        return true;
    }

}
