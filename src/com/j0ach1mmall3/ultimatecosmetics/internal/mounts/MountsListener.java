package com.j0ach1mmall3.ultimatecosmetics.internal.mounts;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Mount;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.util.Vector;

/**
 * Created by j0ach1mmall3 on 12:19 23/08/2015 using IntelliJ IDEA.
 */
public final class MountsListener implements Listener {
    private final Main plugin;

    public MountsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler(plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        Player p = e.getEntity();
        if (api.hasMount(p)) api.getMount(p).remove();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (e.getEntity() instanceof Player) {
                AnimalTamer p = (AnimalTamer) e.getEntity();
                if (this.plugin.getApi().hasMount(p)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof HorseInventory) {
            AnimalTamer p = e.getPlayer();
            if (this.plugin.getApi().hasMount(p)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByMount(EntityDamageByEntityEvent e) {
        if (isMount(e.getDamager())) e.setCancelled(true);
    }

    @EventHandler
    public void onMountCombust(EntityCombustEvent e) {
        if (e.getDuration() != Integer.MAX_VALUE) {
            if (isMount(e.getEntity())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMountExplode(EntityExplodeEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onMountChangeBlock(EntityChangeBlockEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onMountTeleport(EntityTeleportEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onMountPortal(EntityPortalEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() != EntityTargetEvent.TargetReason.CUSTOM) {
            if (isMount(e.getEntity())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMountInteract(EntityInteractEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        Player p = e.getPlayer();
        if (api.hasMount(p)) api.getMount(p).getEntity().teleport(p);
    }

    @EventHandler
    public void onMountTame(EntityTameEvent e) {
        if (isMount(e.getEntity())) e.setCancelled(true);
    }

    private void startScheduler(Main plugin) {
        CosmeticsAPI api = plugin.getApi();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Mount mount : api.getMounts()) {
                Entity ent = mount.getEntity();
                Player p = mount.getPlayer();
                if (ent.getPassenger() == null) {
                    api.getMount(p).remove();
                    return;
                }
                Vector v = p.getLocation().getDirection().setY(0).normalize().multiply(4);
                Location loc = p.getLocation().add(v);
                MountReflection.setNavigation(ent, loc, plugin.getMounts().getMountSpeed());
            }
        }, 0L, 5L);
    }

    private boolean isMount(Entity ent) {
        for (Mount mount : this.plugin.getApi().getMounts()) {
            if (mount.getEntity().getUniqueId().equals(ent.getUniqueId())) return true;
        }
        return false;
    }
}
