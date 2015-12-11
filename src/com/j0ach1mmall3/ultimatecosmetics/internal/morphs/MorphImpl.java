package com.j0ach1mmall3.ultimatecosmetics.internal.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Morph;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.MorphStorage;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 20:38 1/10/2015 using IntelliJ IDEA.
 */
public final class MorphImpl implements Morph {
    private Player player;
    private MorphStorage morphStorage;

    public MorphImpl(Player player, MorphStorage morphStorage) {
        this.player = player;
        this.morphStorage = morphStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public MorphStorage getMorphStorage() {
        return this.morphStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.morphStorage.getPlugin().getApi();
        if (api.hasMorph(this.player)) {
            api.getMorph(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Morph cosmetic = (Morph) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.morphStorage = cosmetic.getMorphStorage();
            if (this.morphStorage.getPlugin().getMorphs().isEnableAbilities()) {
                if (this.player.getInventory().getItem(this.morphStorage.getPlugin().getMorphs().getAbilitySlot()) != null && this.morphStorage.getPlugin().getMorphs().isCheckInSlot()) {
                    this.player.sendMessage(Placeholders.parse(this.morphStorage.getPlugin().getLang().getAlreadyInAbilitySlot(), this.player));
                    return;
                } else {
                    this.player.getInventory().setItem(this.morphStorage.getPlugin().getMorphs().getAbilitySlot(), this.morphStorage.getAbilityItem());
                }
            }
            MobDisguise md = new MobDisguise(DisguiseType.valueOf(this.morphStorage.getMorphType()));
            DisguiseAPI.disguiseIgnorePlayers(this.player, md, this.player);
            api.addMorph(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.morphStorage.getPlugin().getApi();
        if (api.hasMorph(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.player.setFlying(false);
            this.player.setAllowFlight(false);
            api.removeMorph(this);
            DisguiseAPI.undisguiseToAll(this.player);
            if (this.morphStorage.getPlugin().getMorphs().isEnableAbilities()) {
                this.player.getInventory().setItem(this.morphStorage.getPlugin().getMorphs().getAbilitySlot(), null);
            }
        }
    }
}
