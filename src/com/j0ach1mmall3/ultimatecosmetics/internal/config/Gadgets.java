package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 19:36 22/08/2015 using IntelliJ IDEA.
 */
public final class Gadgets extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean disableBatSounds;
    private final boolean keepGadget;
    private final boolean checkInSlot;
    private final int gadgetSlot;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<GadgetStorage> gadgets;

    private final int maxPage;

    public Gadgets(Main plugin) {
        super("gadgets.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.disableBatSounds = this.config.getBoolean("DisableBatSounds");
        this.keepGadget = this.config.getBoolean("KeepGadget");
        this.checkInSlot = this.config.getBoolean("CheckInSlot");
        this.gadgetSlot = this.config.getInt("GadgetSlot");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.gadgets = getGadgetsInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Gadgets config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (GadgetStorage gadget : this.gadgets) {
            if (gadget.getPosition() > biggestSlot) biggestSlot = gadget.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<GadgetStorage> getGadgetsInternal() {
        return this.customConfig.getKeys("Gadgets").stream().map(this::getGadgetByIdentifier).collect(Collectors.toList());
    }

    private GadgetStorage getGadgetByIdentifier(String identifier) {
        String path = "Gadgets." + identifier + '.';
        String item = this.config.getString(path + "Item");
        return new GadgetStorage(
                this.plugin,
                identifier,
                new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                this.config.getBoolean(path + "UseAmmo"),
                this.config.getString(path + "AmmoName"),
                Parsing.parseInt(this.config.getString(path + "Cooldown")),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission")
        );
    }

    public List<Integer> getIntList(String identifier, String value) {
        return this.config.getIntegerList("Gadgets." + identifier + '.' + value);
    }

    public int getIntValue(String identifier, String value) {
        return this.config.getInt("Gadgets." + identifier + '.' + value);
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean getBooleanValue(String identifier, String value) {
        return this.config.getBoolean("Gadgets." + identifier + '.' + value);
    }

    public List<String> getStringList(String identifier, String value) {
        return this.config.getStringList("Gadgets." + identifier + '.' + value);
    }

    public List<GadgetStorage> getGadgets() {
        return Collections.unmodifiableList(this.gadgets);
    }

    public int getRemoveItemPosition() {
        return this.removeItem_Position;
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public boolean isDisableBatSounds() {
        return this.disableBatSounds;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public String getCommand() {
        return this.command;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getGadgetSlot() {
        return this.gadgetSlot;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(GadgetStorage gadget) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", gadget.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public boolean isKeepGadget() {
        return this.keepGadget;
    }

    public boolean isCheckInSlot() {
        return this.checkInSlot;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
