package com.j0ach1mmall3.ultimatecosmetics.api.cosmetics;

import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;

/**
 * Created by j0ach1mmall3 on 20:43 17/08/2015 using IntelliJ IDEA.
 */
public interface Gadget extends Cosmetic {
    GadgetStorage getGadgetStorage();
}
