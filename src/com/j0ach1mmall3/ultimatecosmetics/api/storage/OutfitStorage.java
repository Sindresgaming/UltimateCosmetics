package com.j0ach1mmall3.ultimatecosmetics.api.storage;


import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by j0ach1mmall3 on 3:48 23/08/2015 using IntelliJ IDEA.
 */
public final class OutfitStorage {
    private final Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private CustomItem leggingsItem;
    private CustomItem bootsItem;

    public OutfitStorage(Main plugin, String identifier, CustomItem item, int position, String permission, CustomItem leggingsItem, CustomItem bootsItem) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.leggingsItem = leggingsItem;
        ItemMeta leggingsMeta = leggingsItem.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.leggingsItem.setItemMeta(leggingsMeta);
        this.bootsItem = bootsItem;
        ItemMeta bootsMeta = bootsItem.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.bootsItem.setItemMeta(bootsMeta);
    }

    public Main getPlugin() {
        return this.plugin;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public CustomItem getItem() {
        return this.item;
    }

    public void setItem(CustomItem item) {
        this.item = item;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CustomItem getBootsItem() {
        return this.bootsItem;
    }

    public void setBootsItem(CustomItem bootsItem) {
        this.bootsItem = bootsItem;
    }

    public CustomItem getLeggingsItem() {
        return this.leggingsItem;
    }

    public void setLeggingsItem(CustomItem leggingsItem) {
        this.leggingsItem = leggingsItem;
    }
}
