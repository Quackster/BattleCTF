package org.alexdev.battlectf.commands.types;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.alexdev.battlectf.managers.arena.Arena;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ResetCommand {
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
            arena.reset();
            player.sendMessage(LocaleUtil.getInstance().getArenaReset(name));
        } else {
            player.sendMessage(LocaleUtil.getInstance().getArenaNotFound(name));
        }

        return true;

    }
}
