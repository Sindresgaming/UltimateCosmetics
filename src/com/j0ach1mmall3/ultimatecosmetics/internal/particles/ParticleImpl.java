package com.j0ach1mmall3.ultimatecosmetics.internal.particles;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Particle;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by j0ach1mmall3 on 14:27 23/08/2015 using IntelliJ IDEA.
 */
public final class ParticleImpl implements Particle {
    private Player player;
    private ParticleStorage particleStorage;

    public ParticleImpl(Player player, ParticleStorage particleStorage) {
        this.player = player;
        this.particleStorage = particleStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ParticleStorage getParticleStorage() {
        return this.particleStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.particleStorage.getPlugin().getApi();
        if (api.hasParticle(this.player)) {
            api.getParticle(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Particle cosmetic = (Particle) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.particleStorage = cosmetic.getParticleStorage();
            api.addParticle(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.particleStorage.getPlugin().getApi();
        if (api.hasParticle(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeParticle(this);
        }
    }
}
