package org.alexdev.battlectf.managers.players;

import org.alexdev.battlectf.util.attributes.BattleAttributable;
import org.bukkit.entity.Player;

public class BattlePlayer extends BattleAttributable {
    private final Player player;

    public BattlePlayer(Player player) {
        this.player = player;
    }
}
