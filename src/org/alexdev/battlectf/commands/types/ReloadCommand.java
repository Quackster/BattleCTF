package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "/ctf reset [name]");
            return true;
        }

        String name = args[1];

        if (name.isEmpty()) {
            player.sendMessage(LocaleUtil.getInstance().getNoNameProvided());
            return true;
        }

        Arena arena = ArenaManager.getInstance().getArena(name);

        if (arena != null) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(arena.getConfigFile());

            arena.refreshTeams(conf);
            arena.reset();

            player.sendMessage(LocaleUtil.getInstance().getArenaReloaded(name));
        } else {
            player.sendMessage(LocaleUtil.getInstance().getArenaNotFound(name));
        }

        return true;
    }
}
