package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.ParticleStorage;

/**
 * Created by j0ach1mmall3 on 3:56 23/08/2015 using IntelliJ IDEA.
 */
public interface Particle extends Cosmetic {
    ParticleStorage getParticleStorage();
}
