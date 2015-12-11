package com.j0ach1mmall3.ultimatecosmetics.api.storage;


import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by j0ach1mmall3 on 19:33 22/08/2015 using IntelliJ IDEA.
 */
public final class GadgetStorage {
    private Main plugin;
    private String identifier;
    private CustomItem item;
    private boolean useAmmo;
    private String ammoName;
    private int cooldown;
    private int position;
    private String permission;

    public GadgetStorage(Main plugin, String identifier, CustomItem item, boolean useAmmo, String ammoName, int cooldown, int position, String permission) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.useAmmo = useAmmo;
        this.ammoName = ammoName;
        this.position = position;
        this.cooldown = cooldown;
        this.permission = permission;
    }

    public Main getPlugin() {
        return this.plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
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

    public boolean isUseAmmo() {
        return this.useAmmo;
    }

    public void setUseAmmo(boolean useAmmo) {
        this.useAmmo = useAmmo;
    }

    public CharSequence getAmmoName() {
        return this.ammoName;
    }

    public void setAmmoName(String ammoName) {
        this.ammoName = ammoName;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
