package org.alexdev.battlectf.commands.types;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.managers.schematic.SchematicManager;
import org.alexdev.battlectf.util.BattleAttribute;
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
            player.sendMessage(ChatColor.RED + "There was no name provided for the arena");
            return true;
        }

        Clipboard clipboard = SchematicManager.load(player, name);
        SchematicManager.paste(player, clipboard);

        player.sendMessage(ChatColor.YELLOW + "Arena '" + name + "' has been reset");
        return true;

    }
}
