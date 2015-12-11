package com.j0ach1mmall3.ultimatecosmetics.internal.particles;

import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Particle;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleStorage;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by j0ach1mmall3 on 14:34 23/08/2015 using IntelliJ IDEA.
 */
public final class ParticlesTask extends BukkitRunnable {
    private final Main plugin;

    public ParticlesTask(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Particle particle : this.plugin.getApi().getParticles()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Location l = particle.getPlayer().getLocation();
                l.setY(l.getY() + 0.5);
                ParticleStorage particleStorage = particle.getParticleStorage();
                if (ReflectionAPI.verBiggerThan(1, 8))
                    p.spigot().playEffect(l, Effect.valueOf(particleStorage.getParticle()), particleStorage.getId(), particleStorage.getData(), 0.5f, 0.5f, 0.5f, 0.1f, 10, this.plugin.getParticles().getViewDistance());
                else p.playEffect(l, Effect.valueOf(particleStorage.getParticle()), particleStorage.getData());
            }
        }
    }
}
