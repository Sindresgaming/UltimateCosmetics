package com.j0ach1mmall3.ultimatecosmetics.internal.wardrobe;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Outfit;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.OutfitStorage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j0ach1mmall3 on 10:11 24/08/2015 using IntelliJ IDEA.
 */
public final class WardrobeListener implements Listener {
    private final Main plugin;
    private final Map<String, Outfit> outfitMap = new HashMap<>();

    public WardrobeListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (this.plugin.getApi().hasOutfit(e.getWhoClicked())) {
            if (e.getRawSlot() == 6 || e.getRawSlot() == 7 || e.getRawSlot() == 8) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.plugin.getWardrobe().isKeepOnDeath()) {
            Player p = e.getEntity();
            if (this.plugin.getApi().hasOutfit(p)) {
                Outfit outfit = this.plugin.getApi().getOutfit(p);
                this.outfitMap.put(p.getName(), outfit);
                e.getDrops().remove(outfit.getOutfitStorage().getItem());
                e.getDrops().remove(outfit.getOutfitStorage().getLeggingsItem());
                e.getDrops().remove(outfit.getOutfitStorage().getBootsItem());
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            HumanEntity p = (HumanEntity) e.getEntity();
            if (this.plugin.getApi().hasOutfit(p)) {
                ItemStack chestplate = p.getInventory().getChestplate();
                if (chestplate != null) chestplate.setDurability((short) 0);
                p.getInventory().setChestplate(chestplate);
                ItemStack leggings = p.getInventory().getLeggings();
                if (leggings != null) leggings.setDurability((short) 0);
                p.getInventory().setLeggings(leggings);
                ItemStack boots = p.getInventory().getBoots();
                if (boots != null) boots.setDurability((short) 0);
                p.getInventory().setBoots(boots);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.outfitMap.containsKey(p.getName())) {
            OutfitStorage outfit = this.outfitMap.get(p.getName()).getOutfitStorage();
            p.getInventory().setChestplate(outfit.getItem());
            p.getInventory().setLeggings(outfit.getLeggingsItem());
            p.getInventory().setBoots(outfit.getBootsItem());
            this.outfitMap.remove(p.getName());
        }
    }
}
