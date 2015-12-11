package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.MountStorage;
import org.bukkit.entity.Entity;

/**
 * Created by j0ach1mmall3 on 3:55 23/08/2015 using IntelliJ IDEA.
 */
public interface Mount extends Cosmetic {
    Entity getEntity();

    MountStorage getMountStorage();
}
