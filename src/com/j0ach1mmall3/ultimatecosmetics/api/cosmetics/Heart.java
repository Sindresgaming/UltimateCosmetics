package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.HeartStorage;
import org.bukkit.potion.PotionEffect;

/**
 * Created by j0ach1mmall3 on 3:54 23/08/2015 using IntelliJ IDEA.
 */
public interface Heart extends Cosmetic {
    PotionEffect getColorEffect();

    PotionEffect getEffectsEffect();

    HeartStorage getHeartStorage();

    void reGive();
}
