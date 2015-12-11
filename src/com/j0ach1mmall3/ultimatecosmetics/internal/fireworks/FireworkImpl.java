package com.j0ach1mmall3.ultimatecosmetics.internal.fireworks;

import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Firework;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.FireworkStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Created by j0ach1mmall3 on 16:52 22/08/2015 using IntelliJ IDEA.
 */
public final class FireworkImpl implements Firework {
    private Player player;
    private FireworkStorage fireworkStorage;

    public FireworkImpl(Player player, FireworkStorage fireworkStorage) {
        this.player = player;
        this.fireworkStorage = fireworkStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public FireworkStorage getFireworkStorage() {
        return this.fireworkStorage;
    }

    @Override
    public void give() {
        CosmeticGiveEvent event = new CosmeticGiveEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Firework cosmetic = (Firework) event.getCosmetic();
        this.player = cosmetic.getPlayer();
        this.fireworkStorage = cosmetic.getFireworkStorage();
        org.bukkit.entity.Firework fw = this.player.getWorld().spawn(this.player.getLocation(), org.bukkit.entity.Firework.class);
        fw.setFireworkMeta((FireworkMeta) this.fireworkStorage.getItem().getItemMeta());
    }

    @Override
    public void remove() {
        CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Cosmetic cosmetic = event.getCosmetic();
        this.player = cosmetic.getPlayer();
    }
}
