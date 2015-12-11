package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.TrailStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 14:21 23/08/2015 using IntelliJ IDEA.
 */
public final class Trails extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final double dropInterval;
    private final int removeDelay;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<TrailStorage> trails;

    private final int maxPage;

    public Trails(Main plugin) {
        super("trails.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.dropInterval = this.config.getDouble("DropInterval");
        this.removeDelay = this.config.getInt("RemoveDelay");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.trails = getTrailsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Trails config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (TrailStorage trail : this.trails) {
            if (trail.getPosition() > biggestSlot) biggestSlot = trail.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<TrailStorage> getTrailsInternal() {
        return this.customConfig.getKeys("Trails").stream().map(this::getTrailByIdentifier).collect(Collectors.toList());
    }

    private TrailStorage getTrailByIdentifier(String identifier) {
        String path = "Trails." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new TrailStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission")
        );
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public String getCommand() {
        return this.command;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public Iterable<TrailStorage> getTrails() {
        return Collections.unmodifiableList(this.trails);
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public int getRemoveItemPosition() {
        return this.removeItem_Position;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(TrailStorage trail) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", trail.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public double getDropInterval() {
        return this.dropInterval;
    }

    public int getRemoveDelay() {
        return this.removeDelay;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
