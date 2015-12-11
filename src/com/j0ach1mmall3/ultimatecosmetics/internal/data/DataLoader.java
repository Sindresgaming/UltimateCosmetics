package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import org.bukkit.entity.Player;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public interface DataLoader {
    void loadAmmo(String uuid);

    void unloadAmmo(String uuid);

    int getAmmo(String identifier, String uuid);

    void giveAmmo(String identifier, String uuid, int amount);

    void takeAmmo(String identifier, String uuid, int amount);

    void createAmmo(String uuid);

    void setAmmo(String identifier, String uuid, int amount);

    void giveBackQueue(Player p);

    void updateQueue(Player p, CosmeticsQueue queue);

    void createQueue(Player p);

    void getStacker(Player p, CallbackHandler<Boolean> callbackHandler);

    void createStacker(Player p);

    void setStacker(Player p, boolean stacker);

    void disconnectLoader();
}
