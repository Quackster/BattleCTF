package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CreateCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "/ctf create [name]");
            return true;
        }

        String name = args[1];

        if (name.isEmpty()) {
            player.sendMessage(LocaleUtil.getInstance().getNoNameProvided());
            return true;
        }

        Location first = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA_FIRST, null);
        Location second = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA_SECOND, null);

        if (first == null) {
            player.sendMessage(LocaleUtil.getInstance().getNoFirstPosition());
            return true;
        }

        if (second == null) {
            player.sendMessage(LocaleUtil.getInstance().getNoSecondPosition());
            return true;
        }

        if (SchematicManager.save(player, name, first, second)) {
            try {
                ArenaManager.getInstance().createArena(player, name, first, second);
                player.sendMessage(LocaleUtil.getInstance().getArenaSaved(name));
            } catch (IOException e) {
                player.sendMessage(LocaleUtil.getInstance().getErrorOccurred());
            }
        }

        return true;
    }
}
