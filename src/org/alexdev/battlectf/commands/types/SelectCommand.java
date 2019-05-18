package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        boolean isBuilder = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);
        battlePlayer.set(BattleAttribute.SELECT_ARENA, !isBuilder);

        player.sendMessage(ChatColor.GREEN + "Selecting arena is set to " + (!isBuilder ? "true" : "false"));
        return true;

    }
}
