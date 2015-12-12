package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class FileDataLoader extends ConfigLoader implements DataLoader {
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();


    public FileDataLoader(Main plugin) {
        super("data.yml", plugin);
    }

    @Override
    public void disconnectLoader() {
        //Nothing needed here
    }

    @Override
    public void loadAmmo(String uuid) {
        createAmmo(uuid);
        Map<String, Integer> gadgetAmmo = new HashMap<>();
        for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
            gadgetAmmo.put(gadget.getIdentifier(), this.config.getInt("Ammo." + uuid + '.' + gadget.getIdentifier()));
        }
        this.ammo.put(uuid, gadgetAmmo);
    }

    @Override
    public void unloadAmmo(String uuid) {
        Map<String, Integer> gadgetAmmo = this.ammo.get(uuid);
        this.ammo.remove(uuid);
        if(gadgetAmmo == null) return;
        for(Map.Entry<String, Integer> entry : gadgetAmmo.entrySet()) {
            this.config.set("Ammo." + uuid + '.' + entry.getKey(), entry.getValue());
        }
        this.customConfig.saveConfig(this.config);
    }

    @Override
    public int getAmmo(String identifier, String uuid) {
        return this.ammo.get(uuid).get(identifier);
    }

    @Override
    public void giveAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) + amount <= 99999) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) + amount);
    }

    @Override
    public void takeAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) - amount >= 0) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) - amount);
    }

    @Override
    public void createAmmo(String uuid) {
        if(this.customConfig.getKeys("Ammo") == null || !this.customConfig.getKeys("Ammo").contains(uuid)) {
            for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
                this.config.set("Ammo." + uuid + '.' + gadget.getIdentifier(), 0);
            }
            this.customConfig.saveConfig(this.config);
        }
    }

    @Override
    public void setAmmo(String identifier, String uuid, int amount) {
        this.ammo.get(uuid).put(identifier, amount);
    }

    @Override
    public void giveBackQueue(Player p) {
        if(this.customConfig.getKeys("Queue") == null || !this.customConfig.getKeys("Queue").contains(p.getUniqueId().toString())) new CosmeticsQueue((Main) this.plugin, this.config.getStringList("Queue." + p.getUniqueId())).give(p);
    }

    @Override
    public void updateQueue(Player p, CosmeticsQueue queue) {
        this.config.set("Queue." + p.getUniqueId(), queue.asList());
        this.customConfig.saveConfig(this.config);
        Methods.removeCosmetics(p, (Main) this.plugin);
    }

    @Override
    public void createQueue(Player p) {
        if(this.customConfig.getKeys("Queue") == null || !this.customConfig.getKeys("Queue").contains(p.getUniqueId().toString())) {
            this.config.set("Queue." + p.getUniqueId(), new ArrayList<>());
            this.customConfig.saveConfig(this.config);
        }
    }


    @Override
    public void getStacker(Player p, CallbackHandler<Boolean> callbackHandler) {
        callbackHandler.callback(this.config.getBoolean("Stacker." + p.getUniqueId()));
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        this.config.set("Stacker." + p.getUniqueId(), stacker);
        this.customConfig.saveConfig(this.config);
    }

    @Override
    public void createStacker(Player p) {
        if(this.customConfig.getKeys("Stacker") == null || !this.customConfig.getKeys("Stacker").contains(p.getUniqueId().toString())) {
            this.config.set("Stacker." + p.getUniqueId(), true);
            this.customConfig.saveConfig(this.config);
        }
    }
}
