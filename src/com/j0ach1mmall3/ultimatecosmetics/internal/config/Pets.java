package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.PetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 15:47 23/08/2015 using IntelliJ IDEA.
 */
public final class Pets extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final int teleportInterval;
    private final int teleportDistance;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<PetStorage> pets;

    private final int maxPage;

    public Pets(Main plugin) {
        super("pets.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.teleportInterval = this.config.getInt("TeleportInterval");
        this.teleportDistance = this.config.getInt("TeleportDistance");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.pets = getPetsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Pets config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (PetStorage pet : this.pets) {
            if (pet.getPosition() > biggestSlot) biggestSlot = pet.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<PetStorage> getPetsInternal() {
        return this.customConfig.getKeys("Pets").stream().map(this::getPetByIdentifier).collect(Collectors.toList());
    }

    private PetStorage getPetByIdentifier(String identifier) {
        String path = "Pets." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new PetStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                this.config.getString(path + "PetType"),
                this.config.getStringList(path + "PetData")
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

    public Iterable<PetStorage> getPets() {
        return Collections.unmodifiableList(this.pets);
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

    public CustomItem getNoPermissionItem(PetStorage pet) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", pet.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
