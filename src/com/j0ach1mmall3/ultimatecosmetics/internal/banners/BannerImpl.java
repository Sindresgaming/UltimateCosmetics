package com.j0ach1mmall3.ultimatecosmetics.internal.banners;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Banner;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BannerStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Banners;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 10:49 22/08/2015 using IntelliJ IDEA.
 */
public final class BannerImpl implements Banner {
    private Player player;
    private BannerStorage bannerStorage;

    public BannerImpl(Player player, BannerStorage bannerStorage) {
        this.player = player;
        this.bannerStorage = bannerStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public BannerStorage getBannerStorage() {
        return this.bannerStorage;
    }

    @Override
    public void give() {
        Banners config = this.bannerStorage.getPlugin().getBanners();
        Lang lang = this.bannerStorage.getPlugin().getLang();
        CosmeticsAPI api = this.bannerStorage.getPlugin().getApi();
        if (api.hasBanner(this.player)) {
            api.getBanner(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Banner cosmetic = (Banner) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.bannerStorage = cosmetic.getBannerStorage();
            if (this.player.getInventory().getHelmet() != null && config.isCheckOnHead()) {
                this.player.sendMessage(Placeholders.parse(lang.getAlreadyOnHead(), this.player).replace("%itemname%", this.player.getInventory().getHelmet().getType().toString().toLowerCase().replace("_", " ")));
            } else {
                this.player.getInventory().setHelmet(this.bannerStorage.getItem());
                api.addBanner(this);
            }
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.bannerStorage.getPlugin().getApi();
        if (api.hasBanner(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeBanner(this);
            this.player.getInventory().setHelmet(null);
        }
    }
}
