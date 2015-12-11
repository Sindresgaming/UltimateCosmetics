package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.BalloonStorage;
import org.bukkit.entity.Bat;
import org.bukkit.entity.FallingBlock;

/**
 * Created by j0ach1mmall3 on 20:43 17/08/2015 using IntelliJ IDEA.
 */
public interface Balloon extends Cosmetic {
    Bat getBat();

    FallingBlock getBlock();

    BalloonStorage getBalloonStorage();
}
