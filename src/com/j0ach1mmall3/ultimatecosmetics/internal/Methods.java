package com.j0ach1mmall3.ultimatecosmetics.internal;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

/**
 * Created by j0ach1mmall3 on 5:04 21/08/2015 using IntelliJ IDEA.
 */
public final class Methods {
    private Methods() {

    }

    public static CustomItem getNoPermissionItem(ConfigurationSection config) {
        String path = "NoPermissionItem.";
        String item = config.getString(path + "Item");
        return new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(config.getString(path + "Name")), Placeholders.parse(config.getString(path + "Description")));
    }

    public static CustomItem getRemoveItem(ConfigurationSection config) {
        String path = "RemoveItem.";
        String item = config.getString(path + "Item");
        return new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(config.getString(path + "Name")), Placeholders.parse(config.getString(path + "Description")));
    }

    public static void cleanup(Main plugin) {
        if (plugin.getBabies() == null) return;
        if (plugin.getBabies().getLoggingLevel() >= 1)
            General.sendColoredMessage(plugin, "Cleaning up...", ChatColor.GREEN);
        if (plugin.getGadgets().isEnabled()) plugin.getGadgetsListener().cleanup();
        if (plugin.getMorphs().isEnabled()) plugin.getMorphsListener().cleanup();
        if (plugin.getTrails().isEnabled()) plugin.getTrailsListener().cleanup();
        for (Player p : Bukkit.getOnlinePlayers()) {
            removeCosmetics(p, plugin);
        }
        plugin.getDataLoader().disconnectLoader();
        if (plugin.getBabies().getLoggingLevel() >= 1) General.sendColoredMessage(plugin, "Done!", ChatColor.GREEN);
    }

    public static void removeCosmetics(Player p, Main plugin) {
        CosmeticsAPI api = plugin.getApi();
        if (api.hasBalloon(p)) api.getBalloon(p).remove();
        if (api.hasBanner(p)) api.getBanner(p).remove();
        if (api.hasBowtrail(p)) api.getBowtrail(p).remove();
        if (plugin.isLibsDisguises() && api.hasMorph(p)) api.getMorph(p).remove();
        if (api.hasGadget(p)) p.getInventory().setItem(plugin.getGadgets().getGadgetSlot(), null);
        if (api.hasHat(p)) api.getHat(p).remove();
        if (api.hasHeart(p)) api.getHeart(p).remove();
        if (api.hasMount(p)) api.getMount(p).remove();
        if (api.hasMusic(p)) api.getMusic(p).remove();
        if (api.hasParticle(p)) api.getParticle(p).remove();
        if (api.hasPet(p)) api.getPet(p).remove();
        if (api.hasTrail(p)) api.getTrail(p).remove();
        if (api.hasOutfit(p)) api.getOutfit(p).remove();
        p.closeInventory();
    }

    public static boolean hasPermission(Permissible p, String permission) {
        if (p.hasPermission(permission)) return true;
        String[] components = permission.split("\\.");
        String perm = components[0] + '.';
        for (int i = 1; i < components.length; i++) {
            if (p.hasPermission(perm + '*')) return true;
            if (p.hasPermission('-' + perm + '*')) return false;
            perm = perm + components[i] + '.';
        }
        return false;
    }

    public static boolean isNoPermissionItem(ItemStack noPermissionItem, ItemStack item) {
        return item.hasItemMeta() && noPermissionItem.getType() == item.getType() && noPermissionItem.getDurability() == item.getDurability() && noPermissionItem.getItemMeta().getLore().equals(item.getItemMeta().getLore());
    }
}
