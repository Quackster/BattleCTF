package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaveCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (battlePlayer.getTeam() == null) {
            player.sendMessage(LocaleUtil.getInstance().getNotPlaying());
            return true;
        }

        try {
            battlePlayer.getTeam().leaveTeam(player);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Could not join team!");
        }

        return true;
    }
}
