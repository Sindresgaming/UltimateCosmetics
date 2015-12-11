package com.j0ach1mmall3.ultimatecosmetics.api.storage;


import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 3:34 23/08/2015 using IntelliJ IDEA.
 */
public final class MountStorage {
    private final Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private String mountType;
    private List<String> mountData;

    public MountStorage(Main plugin, String identifier, CustomItem item, int position, String permission, String mountType, List<String> mountData) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.mountType = mountType;
        this.mountData = mountData;
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

    public String getMountType() {
        return this.mountType;
    }

    public void setMountType(String mountType) {
        this.mountType = mountType;
    }

    public Iterable<String> getMountData() {
        return Collections.unmodifiableList(this.mountData);
    }

    public void setMountData(List<String> mountData) {
        this.mountData = mountData;
    }
}
