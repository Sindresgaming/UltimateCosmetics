package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 13:23 20/08/2015 using IntelliJ IDEA.
 */
public final class Config extends ConfigLoader {
    private final boolean updateChecker;
    private final int loggingLevel;
    private final boolean removeCosmeticsOnWorldChange;
    private final boolean removeCosmeticsOnLogOut;
    private final List<String> worldsBlacklist;
    private final boolean giveItemOnJoin;
    private final CustomItem joinItem;
    private final int joinItem_Slot;
    private Sound guiOpenSound = null;
    private Sound guiClickSound = null;
    private final String cosmeticsCommand;
    private final String cosmeticsGui_Name;
    private final int cosmeticsGui_Size;
    private final List<GuiItem> cosmeticsGui_Items;

    public Config(Main plugin) {
        super("config.yml", plugin);
        this.loggingLevel = this.config.getInt("LoggingLevel");
        this.updateChecker = this.config.getBoolean("UpdateChecker");
        if (this.loggingLevel >= 2 && !this.updateChecker)
            General.sendColoredMessage(plugin, "Update Checking is not enabled! You will not receive console notifications!", ChatColor.GOLD);
        this.removeCosmeticsOnWorldChange = this.config.getBoolean("RemoveCosmeticsOnWorldChange");
        if (this.loggingLevel >= 2 && !this.removeCosmeticsOnWorldChange)
            General.sendColoredMessage(plugin, "Removing Cosmetics on World change is not enabled! Players will keep their Cosmetics when they change Worlds!", ChatColor.GOLD);
        this.removeCosmeticsOnLogOut = this.config.getBoolean("RemoveCosmeticsOnLogOut");
        if (this.loggingLevel >= 2 && !this.removeCosmeticsOnLogOut)
            General.sendColoredMessage(plugin, "Removing Cosmetics on Log Out is not enabled! Players will keep their Cosmetics when they log out!", ChatColor.GOLD);
        this.worldsBlacklist = this.config.getStringList("WorldsBlacklist");
        this.giveItemOnJoin = this.config.getBoolean("GiveItemOnJoin");
        this.joinItem = getCustomItem();
        this.joinItem_Slot = this.config.getInt("JoinItem.Slot");
        if (!this.config.getString("GUIOpenSound").isEmpty())
            this.guiOpenSound = Sound.valueOf(this.config.getString("GUIOpenSound"));
        if (!this.config.getString("GUIClickSound").isEmpty())
            this.guiClickSound = Sound.valueOf(this.config.getString("GUIClickSound"));
        this.cosmeticsCommand = this.config.getString("CosmeticsCommand");
        this.cosmeticsGui_Name = this.config.getString("CosmeticsGUI.Name");
        this.cosmeticsGui_Size = this.config.getInt("CosmeticsGUI.Size");
        this.cosmeticsGui_Items = getItems();
        if (this.loggingLevel >= 2) General.sendColoredMessage(plugin, "Main config successfully loaded!", ChatColor.GREEN);
    }

    private CustomItem getCustomItem() {
        String path = "JoinItem.";
        String item = this.config.getString(path + "Item");
        return new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
    }

    private List<GuiItem> getItems() {
        return this.customConfig.getKeys("CosmeticsGUI.Items").stream().map(this::getItemByIdentifier).collect(Collectors.toList());
    }

    private GuiItem getItemByIdentifier(String s) {
        String path = "CosmeticsGUI.Items." + s + '.';
        String item = this.config.getString(path + "Item");
        return new GuiItem(new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description"))),
                Parsing.parseInt(this.config.getString(path + "Position"))
        );
    }

    public GuiItem getGuiItemByItemStack(ItemStack is) {
        for (GuiItem item : this.cosmeticsGui_Items) {
            if (item.getItem().isSimilar(is)) return item;
        }
        return null;
    }

    public String getCommandByItemStack(ItemStack is) {
        for (String s : this.customConfig.getKeys("CosmeticsGUI.Items")) {
            ItemStack item = getItemByIdentifier(s).getItem();
            if (item.isSimilar(is)) return this.config.getString("CosmeticsGUI.Items." + s + ".Command");
        }
        return "";
    }

    public boolean isUpdateChecker() {
        return this.updateChecker;
    }

    public int getLoggingLevel() {
        return this.loggingLevel;
    }

    public boolean isRemoveCosmeticsOnWorldChange() {
        return this.removeCosmeticsOnWorldChange;
    }

    public boolean isGiveItemOnJoin() {
        return this.giveItemOnJoin;
    }

    public CustomItem getJoinItem() {
        return this.joinItem;
    }

    public Sound getGuiOpenSound() {
        return this.guiOpenSound;
    }

    public String getCosmeticsCommand() {
        return this.cosmeticsCommand;
    }

    public String getCosmeticsGuiName() {
        return this.cosmeticsGui_Name;
    }

    public int getCosmeticsGuiSize() {
        return this.cosmeticsGui_Size;
    }

    public Iterable<GuiItem> getCosmeticsGuiItems() {
        return Collections.unmodifiableList(this.cosmeticsGui_Items);
    }

    public Sound getGuiClickSound() {
        return this.guiClickSound;
    }

    public int getJoinItemSlot() {
        return this.joinItem_Slot;
    }

    public boolean isRemoveCosmeticsOnLogOut() {
        return this.removeCosmeticsOnLogOut;
    }

    public Collection<String> getWorldsBlacklist() {
        return Collections.unmodifiableList(this.worldsBlacklist);
    }
}
