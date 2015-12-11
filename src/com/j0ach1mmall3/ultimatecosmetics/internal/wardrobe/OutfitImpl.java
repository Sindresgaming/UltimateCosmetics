package com.j0ach1mmall3.ultimatecosmetics.internal.wardrobe;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Outfit;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.OutfitStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Wardrobe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 9:53 24/08/2015 using IntelliJ IDEA.
 */
public final class OutfitImpl implements Outfit {
    private Player player;
    private OutfitStorage outfitStorage;

    public OutfitImpl(Player player, OutfitStorage outfitStorage) {
        this.player = player;
        this.outfitStorage = outfitStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public OutfitStorage getOutfitStorage() {
        return this.outfitStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.outfitStorage.getPlugin().getApi();
        if (api.hasOutfit(this.player)) {
            api.getOutfit(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Outfit cosmetic = (Outfit) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.outfitStorage = cosmetic.getOutfitStorage();
            Wardrobe config = this.outfitStorage.getPlugin().getWardrobe();
            Lang lang = this.outfitStorage.getPlugin().getLang();
            if (config.isCheckOnBody()) {
                if (this.player.getInventory().getChestplate() != null) {
                    this.player.sendMessage(Placeholders.parse(lang.getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getChestplate().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Chestplate"));
                    return;
                }
                if (this.player.getInventory().getLeggings() != null) {
                    this.player.sendMessage(Placeholders.parse(lang.getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getLeggings().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Leggings"));
                    return;
                }
                if (this.player.getInventory().getBoots() != null) {
                    this.player.sendMessage(Placeholders.parse(lang.getAlreadyOnBody(), this.player).replace("%itemname%", this.player.getInventory().getBoots().getType().toString().toLowerCase().replace("_", " ")).replace("%armorslot%", "Boots"));
                    return;
                }
                this.player.getInventory().setChestplate(this.outfitStorage.getItem());
                this.player.getInventory().setLeggings(this.outfitStorage.getLeggingsItem());
                this.player.getInventory().setBoots(this.outfitStorage.getBootsItem());
                api.addOutfit(this);
            }
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.outfitStorage.getPlugin().getApi();
        if (api.hasOutfit(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeOutfit(this);
            this.player.getInventory().setChestplate(null);
            this.player.getInventory().setLeggings(null);
            this.player.getInventory().setBoots(null);
        }
    }
}
