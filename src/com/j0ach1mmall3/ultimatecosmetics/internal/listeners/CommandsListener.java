package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.internal.gui.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * Created by j0ach1mmall3 on 18:26 20/08/2015 using IntelliJ IDEA.
 */
public final class CommandsListener implements Listener {
    private final Main plugin;

    public CommandsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent e) {
        String message = e.getCommand();
        CommandSender sender = e.getSender();
        if("reload".equalsIgnoreCase(message) || "bukkit:reload".equalsIgnoreCase(message) || message.startsWith("reload ") || message.startsWith("bukkit:reload ")) {
            sender.sendMessage(ChatColor.RED + "UltimateCosmetics does NOT support reloads! Please restart the server instead!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();
        if("reload".equalsIgnoreCase(message) || "bukkit:reload".equalsIgnoreCase(message) || message.startsWith("reload ") || message.startsWith("bukkit:reload ")) {
            p.sendMessage(ChatColor.RED + "UltimateCosmetics does NOT support reloads! Please restart the server instead!");
            e.setCancelled(true);
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getBabies().getCosmeticsCommand())) {
            e.setCancelled(true);
            if (this.plugin.getBabies().getGuiOpenSound() != null)
                Sounds.playSound(p, this.plugin.getBabies().getGuiOpenSound());
            CosmeticsGuiHandler.open(p);
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getBalloons().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getBalloons().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                BalloonsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getBanners().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getBanners().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                BannersGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getBowtrails().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getBowtrails().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                BowtrailsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getMorphs().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getMorphs().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                MorphsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getFireworks().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getFireworks().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                FireworksGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getGadgets().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getGadgets().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                GadgetsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getHats().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getHats().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                HatsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getHearts().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getHearts().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                HeartsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getMounts().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getMounts().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                MountsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getMusic().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getMusic().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                MusicGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getParticles().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getParticles().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                ParticlesGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getPets().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getPets().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                PetsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getTrails().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getTrails().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                TrailsGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
            return;
        }
        if (message.equalsIgnoreCase(this.plugin.getWardrobe().getCommand())) {
            e.setCancelled(true);
            if (this.plugin.getWardrobe().isEnabled() && !this.plugin.getBabies().getWorldsBlacklist().contains(p.getWorld().getName())) {
                WardrobeGuiHandler.open(p, 0);
            } else {
                this.plugin.informPlayerNotEnabled(p);
            }
        }
    }
}
