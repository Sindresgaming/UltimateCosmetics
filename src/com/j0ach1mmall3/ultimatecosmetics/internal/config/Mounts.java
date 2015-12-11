package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.MountStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 11:28 23/08/2015 using IntelliJ IDEA.
 */
public final class Mounts extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final double mountSpeed;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<MountStorage> mounts;

    private final int maxPage;

    public Mounts(Main plugin) {
        super("mounts.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.mountSpeed = this.config.getDouble("MountSpeed");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.mounts = getMountsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Mounts config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (MountStorage mount : this.mounts) {
            if (mount.getPosition() > biggestSlot) biggestSlot = mount.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<MountStorage> getMountsInternal() {
        return this.customConfig.getKeys("Mounts").stream().map(this::getMountByIdentifier).collect(Collectors.toList());
    }

    private MountStorage getMountByIdentifier(String identifier) {
        String path = "Mounts." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new MountStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                this.config.getString(path + "MountType"),
                this.config.getStringList(path + "MountData")
        );
    }

    public Iterable<MountStorage> getMounts() {
        return Collections.unmodifiableList(this.mounts);
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public double getMountSpeed() {
        return this.mountSpeed;
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

    public CustomItem getNoPermissionItem(MountStorage mount) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", mount.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
