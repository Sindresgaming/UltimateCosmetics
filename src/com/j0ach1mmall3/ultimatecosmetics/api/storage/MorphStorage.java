package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by j0ach1mmall3 on 20:35 1/10/2015 using IntelliJ IDEA.
 */
public final class MorphStorage {
    private Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private String morphType;
    private CustomItem abilityItem;
    private int abilityDuration;
    private int abilityCooldown;

    public MorphStorage(Main plugin, String identifier, CustomItem item, int position, String permission, String morphType, CustomItem abilityItem, int abilityDuration, int abilityCooldown) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.morphType = morphType;
        this.abilityItem = abilityItem;
        this.abilityDuration = abilityDuration;
        this.abilityCooldown = abilityCooldown;
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

    public String getMorphType() {
        return this.morphType;
    }

    public void setMorphType(String morphType) {
        this.morphType = morphType;
    }

    public CustomItem getAbilityItem() {
        return this.abilityItem;
    }

    public void setAbilityItem(CustomItem abilityItem) {
        this.abilityItem = abilityItem;
    }

    public int getAbilityDuration() {
        return this.abilityDuration;
    }

    public void setAbilityDuration(int abilityDuration) {
        this.abilityDuration = abilityDuration;
    }

    public int getAbilityCooldown() {
        return this.abilityCooldown;
    }

    public void setAbilityCooldown(int abilityCooldown) {
        this.abilityCooldown = abilityCooldown;
    }
}
