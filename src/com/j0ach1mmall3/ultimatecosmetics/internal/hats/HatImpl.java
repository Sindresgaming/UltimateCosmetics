package com.j0ach1mmall3.ultimatecosmetics.internal.hats;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Hat;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.HatStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Hats;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 10:49 22/08/2015 using IntelliJ IDEA.
 */
public final class HatImpl implements Hat {
    private Player player;
    private HatStorage hatStorage;

    public HatImpl(Player player, HatStorage hatStorage) {
        this.player = player;
        this.hatStorage = hatStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HatStorage getHatStorage() {
        return this.hatStorage;
    }

    @Override
    public void give() {
        Hats config = this.hatStorage.getPlugin().getHats();
        Lang lang = this.hatStorage.getPlugin().getLang();
        CosmeticsAPI api = this.hatStorage.getPlugin().getApi();
        if (api.hasHat(this.player)) {
            api.getHat(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Hat cosmetic = (Hat) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.hatStorage = cosmetic.getHatStorage();
            if (this.player.getInventory().getHelmet() != null && config.isCheckOnHead()) {
                this.player.sendMessage(Placeholders.parse(lang.getAlreadyOnHead(), this.player).replace("%itemname%", this.player.getInventory().getHelmet().getType().toString().toLowerCase().replace("_", " ")));
            } else {
                this.player.getInventory().setHelmet(this.hatStorage.getItem());
                api.addHat(this);
            }
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.hatStorage.getPlugin().getApi();
        if (api.hasHat(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeHat(this);
            this.player.getInventory().setHelmet(null);
        }
    }
}
