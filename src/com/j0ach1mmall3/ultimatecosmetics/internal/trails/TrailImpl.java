package com.j0ach1mmall3.ultimatecosmetics.internal.trails;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Trail;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.TrailStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 17:04 23/08/2015 using IntelliJ IDEA.
 */
public final class TrailImpl implements Trail {
    private Player player;
    private TrailStorage trailStorage;

    public TrailImpl(Player player, TrailStorage trailStorage) {
        this.player = player;
        this.trailStorage = trailStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public TrailStorage getTrailStorage() {
        return this.trailStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.trailStorage.getPlugin().getApi();
        if (api.hasTrail(this.player)) {
            api.getTrail(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Trail cosmetic = (Trail) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.trailStorage = cosmetic.getTrailStorage();
            api.addTrail(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.trailStorage.getPlugin().getApi();
        if (api.hasTrail(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeTrail(this);
        }
    }
}
