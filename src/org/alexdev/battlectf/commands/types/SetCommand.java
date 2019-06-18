package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.BattleCTF;
import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.arena.ArenaTeam;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Paths;

public class SetCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "/ctf set [type] [values...]");
            return true;
        }

        Arena arena = ArenaManager.getInstance().getArenaByLocation(player.getLocation());

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "You are not inside an arena!");
            return true;
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(arena.getConfigFile());

        switch (args[1]) {
            case "addteam": {
                break;

            }
            case "region":
            {
                if (args.length < 5) {
                    player.sendMessage(ChatColor.RED + "/ctf set region [team] [flags] [name]");
                    return true;
                }

                String teamName = args[2];
                ArenaTeam team = arena.getTeamByName(teamName);

                if (team == null) {
                    player.sendMessage(LocaleUtil.getInstance().getTeamNotFound(teamName));
                    return true;
                }

                team.setSpawn(player.getLocation().clone());
                team.save();

                player.sendMessage(LocaleUtil.getInstance().getTeamSaved(teamName));
                break;
            }
            case "spawn":
            {
                String teamName = args[2];
                ArenaTeam team = arena.getTeamByName(teamName);

                if (team == null) {
                    player.sendMessage(LocaleUtil.getInstance().getTeamNotFound(teamName));
                    return true;
                }

                team.setSpawn(player.getLocation().clone());
                team.save();

                player.sendMessage(LocaleUtil.getInstance().getTeamSaved(teamName));
                break;
            }
        }

        return true;

    }
}
