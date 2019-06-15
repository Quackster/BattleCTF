package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.LocaleUtil;
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
            player.sendMessage(LocaleUtil.getInstance().getNoNameProvided());
            return true;
        }

        Arena arena = ArenaManager.getInstance().getArena(name);

        if (arena != null) {
            arena.save(player);
        } else {
            player.sendMessage(LocaleUtil.getInstance().getArenaNotFound(name));
        }

        return true;

    }
}
