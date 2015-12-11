package com.j0ach1mmall3.ultimatecosmetics.api.storage;


import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 3:47 23/08/2015 using IntelliJ IDEA.
 */
public final class PetStorage {
    private final Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private String petType;
    private List<String> petData;

    public PetStorage(Main plugin, String identifier, CustomItem item, int position, String permission, String petType, List<String> petData) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.petType = petType;
        this.petData = petData;
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

    public Iterable<String> getPetData() {
        return Collections.unmodifiableList(this.petData);
    }

    public void setPetData(List<String> petData) {
        this.petData = petData;
    }

    public String getPetType() {
        return this.petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }
}
