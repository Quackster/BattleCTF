package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BuildCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        boolean mode = battlePlayer.getOrDefault(BattleAttribute.BUILD, false);
        battlePlayer.set(BattleAttribute.BUILD, !mode);
        player.sendMessage(LocaleUtil.getInstance().getBuildModeToggle(!mode));
        return true;

    }
}
