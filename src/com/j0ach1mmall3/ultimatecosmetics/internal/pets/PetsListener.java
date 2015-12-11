package com.j0ach1mmall3.ultimatecosmetics.internal.pets;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Pet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PetsListener implements Listener {
    private final Main plugin;
    private final Collection<String> renamingPlayers = new ArrayList<>();

    public PetsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startScheduler(plugin);
    }

    @EventHandler
    public void onPetDamage(EntityDamageEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByPet(EntityDamageByEntityEvent e) {
        if (isPet(e.getDamager())) e.setCancelled(true);
    }

    @EventHandler
    public void onPetCombust(EntityCombustEvent e) {
        if (e.getDuration() != Integer.MAX_VALUE) {
            if (isPet(e.getEntity())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPetExplode(EntityExplodeEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPetChangeBlock(EntityChangeBlockEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPetTeleport(EntityTeleportEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPetPortal(EntityPortalEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() != EntityTargetEvent.TargetReason.CUSTOM) {
            if (isPet(e.getEntity())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPetInteract(EntityInteractEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractPet(PlayerInteractEntityEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        Player p = e.getPlayer();
        if (api.hasPet(p)) {
            Pet pet = api.getPet(p);
            e.setCancelled(true);
            if (e.getRightClicked().getUniqueId().equals(pet.getEntity().getUniqueId()) && p.hasPermission("uc.renamepet")) {
                if (!this.renamingPlayers.contains(p.getName())) {
                    p.sendMessage(Placeholders.parse(this.plugin.getLang().getRenamePet(), p));
                    this.renamingPlayers.add(p.getName());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        Player p = e.getPlayer();
        if (this.renamingPlayers.contains(p.getName())) {
            this.renamingPlayers.remove(p.getName());
            if (api.hasPet(p)) {
                Pet pet = api.getPet(p);
                e.setCancelled(true);
                pet.getEntity().setCustomName(Placeholders.parse(e.getMessage(), p));
                p.sendMessage(Placeholders.parse(this.plugin.getLang().getSuccessfulRename(), p).replace("%petname%", Placeholders.parse(e.getMessage(), p)));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        Player p = e.getPlayer();
        if (api.hasPet(p)) api.getPet(p).getEntity().teleport(p);
    }

    @EventHandler
    public void onPetTame(EntityTameEvent e) {
        if (isPet(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        this.renamingPlayers.remove(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        this.renamingPlayers.remove(e.getPlayer().getName());
    }

    private void startScheduler(Main plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Pet pet : plugin.getApi().getPets()) {
                Entity ent = pet.getEntity();
                Player p = pet.getPlayer();
                if (!ent.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) || ent.getLocation().distance(p.getLocation()) >= plugin.getPets().getTeleportDistance()) {
                    ent.teleport(p);
                }
            }
        }, 0L, plugin.getPets().getTeleportInterval() * 20);
    }

    private boolean isPet(Entity ent) {
        for (Pet pet : this.plugin.getApi().getPets()) {
            if (pet.getEntity().getUniqueId().equals(ent.getUniqueId())) return true;
        }
        return false;
    }
}
