package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BowtrailStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 16:11 31/10/2015 using IntelliJ IDEA.
 */
public final class Bowtrails extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final double updateInterval;
    private final int viewDistance;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<BowtrailStorage> bowtrails;

    private final int maxPage;

    public Bowtrails(Main plugin) {
        super("bowtrails.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.updateInterval = this.config.getDouble("UpdateInterval");
        this.viewDistance = this.config.getInt("ViewDistance");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.bowtrails = getBowtrailsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Bowtrails config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (BowtrailStorage bowtrail : this.bowtrails) {
            if (bowtrail.getPosition() > biggestSlot) biggestSlot = bowtrail.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<BowtrailStorage> getBowtrailsInternal() {
        return this.customConfig.getKeys("Bowtrails").stream().map(this::getBowtrailByIdentifier).collect(Collectors.toList());
    }

    private BowtrailStorage getBowtrailByIdentifier(String identifier) {
        String path = "Bowtrails." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new BowtrailStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                this.config.getString(path + "Bowtrail"),
                Parsing.parseInt(this.config.getString(path + "ID")),
                Parsing.parseInt(this.config.getString(path + "Data"))
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

    public Iterable<BowtrailStorage> getBowtrails() {
        return Collections.unmodifiableList(this.bowtrails);
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

    public CustomItem getNoPermissionItem(BowtrailStorage bowtrail) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", bowtrail.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public double getUpdateInterval() {
        return this.updateInterval;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
