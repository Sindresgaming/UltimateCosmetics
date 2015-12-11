package com.j0ach1mmall3.ultimatecosmetics.internal.hats;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Hat;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Hats;
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
public final class HatsListener implements Listener {
    private final Main plugin;
    private final Map<String, Hat> hatMap = new HashMap<>();

    public HatsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (this.plugin.getApi().hasHat(e.getWhoClicked())) {
            if (e.getRawSlot() == 5) {
                e.setCancelled(true);
                p.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Hats config = this.plugin.getHats();
        CosmeticsAPI api = this.plugin.getApi();
        if (config.isKeepOnDeath()) {
            Player p = e.getEntity();
            if (api.hasHat(p)) {
                Hat hat = api.getHat(p);
                this.hatMap.put(p.getName(), hat);
                e.getDrops().remove(hat.getHatStorage().getItem());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.hatMap.containsKey(p.getName())) {
            p.getInventory().setHelmet(this.hatMap.get(p.getName()).getHatStorage().getItem());
            this.hatMap.remove(p.getName());
        }
    }
}
