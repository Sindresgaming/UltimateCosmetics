package com.j0ach1mmall3.ultimatecosmetics.api.storage;

import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by j0ach1mmall3 on 14:49 31/10/2015 using IntelliJ IDEA.
 */
public final class BowtrailStorage {
    private final Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private String particle;
    private int id;
    private int data;

    public BowtrailStorage(Main plugin, String identifier, CustomItem item, int position, String permission, String particle, int id, int data) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.particle = particle;
        this.id = id;
        this.data = data;
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

    public String getParticle() {
        return this.particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
