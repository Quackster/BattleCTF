package org.alexdev.battlectf.commands.types;

import org.alexdev.battlectf.managers.players.BattlePlayer;
import org.alexdev.battlectf.util.LocaleUtil;
import org.alexdev.battlectf.util.attributes.BattleAttribute;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static boolean onCommand(BattlePlayer battlePlayer, Player player, String[] args) {
        boolean mode = battlePlayer.getOrDefault(BattleAttribute.SELECT_ARENA, false);
        battlePlayer.set(BattleAttribute.SELECT_ARENA, !mode);
        player.sendMessage(LocaleUtil.getInstance().getSelectingToggle(!mode));
        return true;

    }
}
