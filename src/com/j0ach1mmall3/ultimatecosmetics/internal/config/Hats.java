package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.HatStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 4:01 23/08/2015 using IntelliJ IDEA.
 */
public final class Hats extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean checkOnHead;
    private final boolean keepOnDeath;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<HatStorage> hats;

    private final int maxPage;

    public Hats(Main plugin) {
        super("hats.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.checkOnHead = this.config.getBoolean("CheckOnHead");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.hats = getHatsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Hats config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (HatStorage hat : this.hats) {
            if (hat.getPosition() > biggestSlot) biggestSlot = hat.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<HatStorage> getHatsInternal() {
        return this.customConfig.getKeys("Hats").stream().map(this::getHatByIdentifier).collect(Collectors.toList());
    }

    private HatStorage getHatByIdentifier(String identifier) {
        String path = "Hats." + identifier + '.';
        String item = this.config.getString(path + "Item");
        CustomItem customItem = new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        if (customItem.getType() == Material.SKULL_ITEM && customItem.getDurability() == 3) {
            SkullMeta skullMeta = (SkullMeta) customItem.getItemMeta();
            skullMeta.setOwner(this.config.getString(path + "SkullOwner"));
            customItem.setItemMeta(skullMeta);
        }
        return new HatStorage(
                this.plugin,
                identifier,
                customItem,
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission")
        );
    }

    public Iterable<HatStorage> getHats() {
        return Collections.unmodifiableList(this.hats);
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

    public boolean isCheckOnHead() {
        return this.checkOnHead;
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

    public CustomItem getNoPermissionItem(HatStorage hat) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", hat.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
