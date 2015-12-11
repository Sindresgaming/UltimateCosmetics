package com.j0ach1mmall3.ultimatecosmetics.internal.bowtrails;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Bowtrail;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BowtrailStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 16:10 31/10/2015 using IntelliJ IDEA.
 */
public final class BowtrailImpl implements Bowtrail {
    private Player player;
    private BowtrailStorage bowtrailStorage;

    public BowtrailImpl(Player player, BowtrailStorage bowtrailStorage) {
        this.player = player;
        this.bowtrailStorage = bowtrailStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public BowtrailStorage getBowtrailStorage() {
        return this.bowtrailStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.bowtrailStorage.getPlugin().getApi();
        if (api.hasBowtrail(this.player)) {
            api.getBowtrail(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Bowtrail cosmetic = (Bowtrail) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.bowtrailStorage = cosmetic.getBowtrailStorage();
            api.addBowtrail(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.bowtrailStorage.getPlugin().getApi();
        if (api.hasBowtrail(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeBowtrail(this);
        }
    }
}
