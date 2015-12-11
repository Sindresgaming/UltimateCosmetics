package com.j0ach1mmall3.ultimatecosmetics.internal.banners;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Banner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j0ach1mmall3 on 5:10 21/08/2015 using IntelliJ IDEA.
 */
public final class BannersListener implements Listener {
    private final Main plugin;
    private final Map<String, Banner> bannerMap = new HashMap<>();

    public BannersListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (this.plugin.getApi().hasBanner(e.getWhoClicked())) {
            if (e.getRawSlot() == 5) {
                e.setCancelled(true);
                p.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.plugin.getBanners().isKeepOnDeath()) {
            Player p = e.getEntity();
            if (this.plugin.getApi().hasBanner(p)) {
                Banner banner = this.plugin.getApi().getBanner(p);
                this.bannerMap.put(p.getName(), banner);
                e.getDrops().remove(banner.getBannerStorage().getItem());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.bannerMap.containsKey(p.getName())) {
            p.getInventory().setHelmet(this.bannerMap.get(p.getName()).getBannerStorage().getItem());
            this.bannerMap.remove(p.getName());
        }
    }
}
