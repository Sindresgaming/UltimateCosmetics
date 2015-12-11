package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.redis.RedisLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 6/11/2015
 */
public final class RedisDataLoader extends RedisLoader implements DataLoader {
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();
    private final String ammoName;
    private final String queueName;
    private final String stackerName;

    public RedisDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(String uuid) {
        Map<String, Integer> gadgetAmmo = new HashMap<>();
        for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
            this.redis.exists(this.ammoName + '.' + uuid + '.' + gadget.getIdentifier(), b -> {
                if(b) {
                    this.redis.get(this.ammoName + '.' + uuid + '.' + gadget.getIdentifier(), s -> {
                        gadgetAmmo.put(gadget.getIdentifier(), Parsing.parseInt(s));
                        this.ammo.put(uuid, gadgetAmmo);
                    });
                } else {
                    gadgetAmmo.put(gadget.getIdentifier(), 0);
                    this.ammo.put(uuid, gadgetAmmo);
                }
            });
        }
    }

    @Override
    public void unloadAmmo(String uuid) {
        Map<String, Integer> gadgetAmmo = this.ammo.get(uuid);
        this.ammo.remove(uuid);
        for(Map.Entry<String, Integer> entry : gadgetAmmo.entrySet()) {
            this.redis.set(this.ammoName + '.' + uuid + '.' + entry.getKey(), String.valueOf(entry.getValue()));
        }
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
        this.redis.exists(this.ammoName + '.' + uuid + ".enderbow", b -> {
            if(!b) {
                for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
                    this.redis.set(this.ammoName + '.' + uuid + '.' + gadget.getIdentifier().toLowerCase(), String.valueOf(0));
                }
            }
        });
    }

    @Override
    public void setAmmo(String identifier, String uuid, int amount) {
        this.ammo.get(uuid).put(identifier, amount);
    }

    @Override
    public void giveBackQueue(Player p) {
        this.redis.exists(this.queueName + '.' + p.getUniqueId(), b -> {
            if(b) {
                this.redis.get(this.queueName + '.' + p.getUniqueId(), s -> {
                    Bukkit.getScheduler().callSyncMethod(this.plugin, (Callable<Void>) () -> {
                        new CosmeticsQueue((Main) this.plugin, s).give(p);
                        return null;
                    });
                });
            }
        });
    }

    @Override
    public void updateQueue(Player p, CosmeticsQueue queue) {
        Methods.removeCosmetics(p, (Main) this.plugin);
        this.redis.set(this.queueName + '.' + p.getUniqueId().toString(), queue.asString());
    }

    @Override
    public void createQueue(Player p) {
        this.redis.exists(this.queueName + '.' + p.getUniqueId(), b -> {
            if(!b) this.redis.set(this.queueName + '.' + p.getUniqueId(),"||||||||||||");
        });
    }

    @Override
    public void getStacker(Player p, CallbackHandler<Boolean> callbackHandler) {
        this.redis.get(this.stackerName + '.' + p.getUniqueId(), s -> callbackHandler.callback(Boolean.valueOf(s)));
    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        this.redis.set(this.stackerName + '.' + p.getUniqueId(), String.valueOf(stacker));
    }

    @Override
    public void createStacker(Player p) {
        this.redis.exists(this.stackerName + '.' + p.getUniqueId(), b -> {
            if(!b) setStacker(p, true);
        });
    }
}
