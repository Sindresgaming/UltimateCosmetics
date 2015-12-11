package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * Created by j0ach1mmall3 on 18:10 3/10/2015 using IntelliJ IDEA.
 */
public final class Pagination extends ConfigLoader {
    private final GuiItem homeItem;
    private final GuiItem previousItem;
    private final GuiItem nextItem;

    public Pagination(Main plugin) {
        super("pagination.yml", plugin);
        this.homeItem = getGuiItem("HomeItem");
        this.previousItem = getGuiItem("PreviousItem");
        this.nextItem = getGuiItem("NextItem");
    }

    private GuiItem getGuiItem(String identifier) {
        String path = identifier + '.';
        return new GuiItem(new CustomItem(Parsing.parseMaterial(this.config.getString(path + "Item")), 1, Parsing.parseData(this.config.getString(path + "Item")), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                this.config.getInt(path + "Position"));
    }

    public GuiItem getHomeItem() {
        return this.homeItem;
    }

    public GuiItem getPreviousItem() {
        return this.previousItem;
    }

    public GuiItem getNextItem() {
        return this.nextItem;
    }
}
