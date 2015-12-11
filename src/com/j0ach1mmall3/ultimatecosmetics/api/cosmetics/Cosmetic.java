package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 17:39 13/10/2015 using IntelliJ IDEA.
 */
public interface Cosmetic {
    Player getPlayer();

    void give();

    void remove();
}
