package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.PetStorage;
import org.bukkit.entity.Creature;

/**
 * Created by j0ach1mmall3 on 3:57 23/08/2015 using IntelliJ IDEA.
 */
public interface Pet extends Cosmetic {
    Creature getEntity();

    PetStorage getPetStorage();
}
