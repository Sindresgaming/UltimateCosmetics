package com.j0ach1mmall3.ultimatecosmetics.internal.bowtrails;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Bowtrail;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BowtrailStorage;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j0ach1mmall3 on 16:23 31/10/2015 using IntelliJ IDEA.
 */
public final class BowtrailsListener implements Listener {
    private final Main plugin;
    private final Map<Arrow, Bowtrail> arrowsMap = new HashMap<>();

    public BowtrailsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                for (Map.Entry<Arrow, Bowtrail> arrowBowtrailEntry : BowtrailsListener.this.arrowsMap.entrySet()) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Location l = arrowBowtrailEntry.getKey().getLocation();
                        BowtrailStorage bowtrailStorage = BowtrailsListener.this.arrowsMap.get(arrowBowtrailEntry.getKey()).getBowtrailStorage();
                        if (ReflectionAPI.verBiggerThan(1, 8)) p.spigot().playEffect(l, Effect.valueOf(bowtrailStorage.getParticle()), bowtrailStorage.getId(), bowtrailStorage.getData(), 0.0f, 0.0f, 0.0f, 0.1f, 1, plugin.getParticles().getViewDistance());
                        else p.playEffect(l, Effect.valueOf(bowtrailStorage.getParticle()), bowtrailStorage.getData());
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, (long) (plugin.getBowtrails().getUpdateInterval() * 20.0), (long) (plugin.getBowtrails().getUpdateInterval() * 20.0));
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            if (arrow.getShooter() instanceof Player) {
                AnimalTamer p = (AnimalTamer) arrow.getShooter();
                if (this.plugin.getApi().hasBowtrail(p)) this.arrowsMap.put(arrow, this.plugin.getApi().getBowtrail(p));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            this.arrowsMap.remove(e.getEntity());
        }
    }
}
