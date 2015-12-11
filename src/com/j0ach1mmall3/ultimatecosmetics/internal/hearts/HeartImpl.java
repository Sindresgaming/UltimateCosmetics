package com.j0ach1mmall3.ultimatecosmetics.internal.hearts;

import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Heart;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.HeartStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * Created by j0ach1mmall3 on 10:10 23/08/2015 using IntelliJ IDEA.
 */
public final class HeartImpl implements Heart {
    private Player player;
    private PotionEffect colorEffect = null;
    private PotionEffect effectsEffect = null;
    private final HashMap<Integer, Integer> amplifiers = new HashMap<>();
    private HeartStorage heartStorage;

    public HeartImpl(Player player, HeartStorage heartStorage) {
        this.player = player;
        this.amplifiers.put(1, 4);
        this.amplifiers.put(2, 9);
        this.amplifiers.put(3, 14);
        this.amplifiers.put(4, 19);
        this.heartStorage = heartStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public PotionEffect getEffectsEffect() {
        return this.effectsEffect;
    }

    @Override
    public PotionEffect getColorEffect() {
        return this.colorEffect;
    }

    @Override
    public HeartStorage getHeartStorage() {
        return this.heartStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.heartStorage.getPlugin().getApi();
        if (api.hasHeart(this.player)) {
            api.getHeart(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Heart cosmetic = (Heart) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.heartStorage = cosmetic.getHeartStorage();
            this.colorEffect = cosmetic.getColorEffect();
            this.effectsEffect = cosmetic.getEffectsEffect();
            addPotionEffect();
            Sounds.playSound(this.player, Sound.ANVIL_USE);
            api.addHeart(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.heartStorage.getPlugin().getApi();
        if (api.hasHeart(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            removePotionEffect();
            api.removeHeart(this);
        }
    }

    @Override
    public void reGive() {
        removePotionEffect();
        addPotionEffect();
    }

    private void addPotionEffect() {
        int rows = this.heartStorage.getRows();
        if (rows > 4) rows = 4;
        if (rows < 1) rows = 1;
        HeartColor color = HeartColor.valueOf(this.heartStorage.getColor());
        HeartEffect effect = HeartEffect.valueOf(this.heartStorage.getEffect());
        this.player.setMaxHealth(rows * 20);
        this.player.setHealth(this.player.getMaxHealth());
        if (color == HeartColor.YELLOW) {
            this.player.setMaxHealth(0.5);
            this.colorEffect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, this.amplifiers.get(rows));
            this.player.addPotionEffect(this.colorEffect);
            this.player.damage(1.5);
        }
        if (color != HeartColor.RED) {
            this.colorEffect = new PotionEffect(PotionEffectType.getByName(color.getPotionEffect()), Integer.MAX_VALUE, 0);
            this.player.addPotionEffect(this.colorEffect);
        }
        if (effect != HeartEffect.NONE) {
            this.effectsEffect = new PotionEffect(PotionEffectType.getByName(effect.getPotionEffect()), Integer.MAX_VALUE, 0);
            this.player.addPotionEffect(this.effectsEffect);
        }
        this.player.damage(0.0);
    }

    private void removePotionEffect() {
        int rows = this.heartStorage.getRows();
        if (rows > 4) rows = 4;
        if (rows < 1) rows = 1;
        HeartColor color = HeartColor.valueOf(this.heartStorage.getColor());
        HeartEffect effect = HeartEffect.valueOf(this.heartStorage.getEffect());
        if (rows > 1 || color == HeartColor.YELLOW) {
            this.player.setMaxHealth(20.0);
            this.player.setHealth(this.player.getMaxHealth());
        }
        if (color != HeartColor.RED && this.colorEffect != null) this.player.removePotionEffect(this.colorEffect.getType());
        if (effect != HeartEffect.NONE && this.effectsEffect != null) this.player.removePotionEffect(this.effectsEffect.getType());
    }
}
