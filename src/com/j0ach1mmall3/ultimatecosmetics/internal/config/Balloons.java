package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BalloonStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 11:10 20/08/2015 using IntelliJ IDEA.
 */
public final class Balloons extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean disableBatSounds;
    private final int teleportInterval;
    private final int teleportDistance;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<BalloonStorage> balloons;

    private final int maxPage;

    public Balloons(Main plugin) {
        super("balloons.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.disableBatSounds = this.config.getBoolean("DisableBatSounds");
        this.teleportInterval = this.config.getInt("TeleportInterval");
        this.teleportDistance = this.config.getInt("TeleportDistance");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.balloons = getBalloonsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Balloons config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (BalloonStorage balloon : this.balloons) {
            if (balloon.getPosition() > biggestSlot) biggestSlot = balloon.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<BalloonStorage> getBalloonsInternal() {
        return this.customConfig.getKeys("Balloons").stream().map(this::getBalloonByIdentifier).collect(Collectors.toList());
    }

    private BalloonStorage getBalloonByIdentifier(String identifier) {
        String path = "Balloons." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new BalloonStorage(
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

    public boolean isDisableBatSounds() {
        return this.disableBatSounds;
    }

    public int getTeleportInterval() {
        return this.teleportInterval;
    }

    public int getTeleportDistance() {
        return this.teleportDistance;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public Iterable<BalloonStorage> getBalloons() {
        return Collections.unmodifiableList(this.balloons);
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

    public CustomItem getNoPermissionItem(BalloonStorage balloon) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", balloon.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
