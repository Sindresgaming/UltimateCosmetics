package com.j0ach1mmall3.ultimatecosmetics.internal.hearts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Heart;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.HeartStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Hearts;
import org.bukkit.Bukkit;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j0ach1mmall3 on 10:21 23/08/2015 using IntelliJ IDEA.
 */
public final class HeartsListener implements Listener {
    private final Main plugin;
    private final Map<String, Heart> heartsMap = new HashMap<>();

    public HeartsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Hearts config = this.plugin.getHearts();
        CosmeticsAPI api = this.plugin.getApi();
        if (e.getEntity() instanceof Player) {
            AnimalTamer p = (AnimalTamer) e.getEntity();
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause == null) {
                return;
            }
            if (api.hasHeart(p)) {
                HeartStorage heart = api.getHeart(p).getHeartStorage();
                if (cause.name().equalsIgnoreCase(HeartColor.valueOf(heart.getColor()).getPotionEffect())) {
                    e.setCancelled(true);
                    return;
                }
                if (config.isScaleDamage()) {
                    int rows = heart.getRows();
                    e.setDamage(e.getDamage() * rows);
                }
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        if (e.getEntity() instanceof Player) {
            AnimalTamer p = (AnimalTamer) e.getEntity();
            if (api.hasHeart(p)) {
                if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
                    HeartStorage heart = api.getHeart(p).getHeartStorage();
                    if ("BOUNCING".equals(heart.getEffect())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Hearts config = this.plugin.getHearts();
        CosmeticsAPI api = this.plugin.getApi();
        if (config.isKeepOnDeath()) {
            Player p = e.getEntity();
            if (api.hasHeart(p)) {
                Heart heart = api.getHeart(p);
                this.heartsMap.put(p.getName(), heart);
                heart.remove();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.heartsMap.containsKey(p.getName())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                Heart heart = this.heartsMap.get(p.getName());
                heart.give();
                this.heartsMap.remove(p.getName());
            }, 1L);
        }
    }

    private void startScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            for (Heart heart : this.plugin.getApi().getHearts()) {
                Player p = heart.getPlayer();
                if (heart.getColorEffect() != null && !p.hasPotionEffect(heart.getColorEffect().getType()) || heart.getEffectsEffect() != null && !p.hasPotionEffect(heart.getEffectsEffect().getType())) heart.reGive();
            }
        }, 0L, 10L);
    }
}
