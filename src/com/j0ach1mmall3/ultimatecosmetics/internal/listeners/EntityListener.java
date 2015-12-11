package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by j0ach1mmall3 on 12:15 23/08/2015 using IntelliJ IDEA.
 */
public final class EntityListener implements Listener {

    public EntityListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            e.setCancelled(false);
        }
    }
}
