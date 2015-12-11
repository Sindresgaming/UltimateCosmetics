package com.j0ach1mmall3.ultimatecosmetics.api.storage;


import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by j0ach1mmall3 on 3:34 23/08/2015 using IntelliJ IDEA.
 */
public final class HeartStorage {
    private final Main plugin;
    private String identifier;
    private CustomItem item;
    private int position;
    private String permission;
    private int rows;
    private String color;
    private String effect;

    public HeartStorage(Main plugin, String identifier, CustomItem item, int position, String permission, int rows, String color, String effect) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.item = item;
        ItemMeta im = item.getItemMeta();
        if(ReflectionAPI.verBiggerThan(1, 8)) im.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        this.item.setItemMeta(im);
        this.position = position;
        this.permission = permission;
        this.rows = rows;
        this.color = color;
        this.effect = effect;
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

    public String getEffect() {
        return this.effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
