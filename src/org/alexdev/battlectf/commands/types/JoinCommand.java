package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JoinCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        Arena arena = null;

        if (args.length > 1) {
            String name = args[1];

            if (name.isEmpty()) {
                player.sendMessage(LocaleUtil.getInstance().getNoNameProvided());
                return true;
            }

            arena = ArenaManager.getInstance().getArena(name);
        } else {
            List<Arena> sortedList = new ArrayList<>(ArenaManager.getInstance().getArenas().values());
            sortedList.sort(Comparator.comparingInt((Arena a) -> a.getAllPlayers().size()).reversed());

            if (sortedList.size() > 0) {
                arena = sortedList.get(0);
            }
        }

        if (arena == null) {
            player.sendMessage(LocaleUtil.getInstance().getNoGamesToJoin());
            return true;
        }

        if (battlePlayer.getTeam() != null) {
            player.sendMessage(LocaleUtil.getInstance().getAlreadyPlaying());
            return true;
        }

        arena.joinTeam(player, null);
        return true;
    }
}
