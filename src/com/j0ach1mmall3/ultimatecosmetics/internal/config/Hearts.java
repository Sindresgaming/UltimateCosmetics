package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.HeartStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 10:22 23/08/2015 using IntelliJ IDEA.
 */
public final class Hearts extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean keepOnDeath;
    private final boolean scaleDamage;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<HeartStorage> hearts;

    private final int maxPage;

    public Hearts(Main plugin) {
        super("hearts.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
        this.scaleDamage = this.config.getBoolean("ScaleDamage");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.hearts = getHeartsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Hearts config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (HeartStorage heart : this.hearts) {
            if (heart.getPosition() > biggestSlot) biggestSlot = heart.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<HeartStorage> getHeartsInternal() {
        return this.customConfig.getKeys("Hearts").stream().map(this::getHeartByIdentifier).collect(Collectors.toList());
    }

    private HeartStorage getHeartByIdentifier(String identifier) {
        String path = "Hearts." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new HeartStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), this.config.getInt(path + "Rows"), Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                this.config.getInt(path + "Rows"),
                this.config.getString(path + "Color"),
                this.config.getString(path + "Effect")
        );
    }

    public Iterable<HeartStorage> getHearts() {
        return Collections.unmodifiableList(this.hearts);
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }

    public boolean isScaleDamage() {
        return this.scaleDamage;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getRemoveItemPosition() {
        return this.removeItem_Position;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(HeartStorage heart) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", heart.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}