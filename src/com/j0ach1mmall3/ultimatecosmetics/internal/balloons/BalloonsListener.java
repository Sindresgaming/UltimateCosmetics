package com.j0ach1mmall3.ultimatecosmetics.internal.balloons;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Balloon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.stream.Collectors;

public final class BalloonsListener implements Listener {
    private final Main plugin;

    public BalloonsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler();
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
    }

    @EventHandler
    public void onEntityUnleash(PlayerUnleashEntityEvent e) {
        Entity ent = e.getEntity();
        if(ent.hasMetadata("Balloon") || ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if(ent.hasMetadata("Balloon") || ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
        if(ent.getVehicle() != null && ent.getVehicle().hasMetadata("Balloon")) {
            Player owner = Bukkit.getPlayer(ent.getVehicle().getMetadata("Balloon").get(0).asString());
            Balloon balloon = this.plugin.getApi().getBalloon(owner);
            balloon.give();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.getApi().hasBalloon(p)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.FENCE) e.setCancelled(true);
            }
        }
    }

    private void startScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            List<Balloon> balloons = this.plugin.getApi().getBalloons().stream().collect(Collectors.toList());
            for (Balloon balloon : balloons) {
                Bat bat = balloon.getBat();
                Player p = balloon.getPlayer();
                if (bat != null) {
                    if (!bat.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || bat.getLocation().distance(p.getLocation()) >= this.plugin.getBalloons().getTeleportDistance()) {
                        balloon.give();
                    }
                }
            }
        }, 0L, this.plugin.getBalloons().getTeleportInterval() * 20);
    }
}
