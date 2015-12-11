package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.OutfitStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 16:58 23/08/2015 using IntelliJ IDEA.
 */
public final class Wardrobe extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean checkOnBody;
    private final boolean keepOnDeath;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private final List<OutfitStorage> wardrobe;

    private final int maxPage;

    public Wardrobe(Main plugin) {
        super("wardrobe.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.checkOnBody = this.config.getBoolean("CheckOnBody");
        this.keepOnDeath = this.config.getBoolean("KeepOnDeath");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.wardrobe = getWardrobeInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Wardrobe config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (OutfitStorage outfit : this.wardrobe) {
            if (outfit.getPosition() > biggestSlot) biggestSlot = outfit.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<OutfitStorage> getWardrobeInternal() {
        return this.customConfig.getKeys("Outfits").stream().map(this::getOutfitByIdentifier).collect(Collectors.toList());
    }

    private CustomItem getOutfitItem(String identifier) {
        String path = "Outfits." + identifier + '.';
        String item = this.config.getString(path + "ChestPlate.Item");
        CustomItem is = new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        if (is.getType() == Material.LEATHER_CHESTPLATE) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(getColor(identifier, "ChestPlate"));
            is.setItemMeta(meta);
        }
        return is;
    }

    private CustomItem getLeggingsItem(String identifier) {
        String path = "Outfits." + identifier + '.';
        String item = this.config.getString(path + "Leggings.Item");
        CustomItem is = new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        if (is.getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(getColor(identifier, "Leggings"));
            is.setItemMeta(meta);
        }
        return is;
    }

    private CustomItem getBootsItem(String identifier) {
        String path = "Outfits." + identifier + '.';
        String item = this.config.getString(path + "Boots.Item");
        CustomItem is = new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        if (is.getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(getColor(identifier, "Boots"));
            is.setItemMeta(meta);
        }
        return is;
    }

    private OutfitStorage getOutfitByIdentifier(String identifier) {
        String path = "Outfits." + identifier + '.';
        return new OutfitStorage(
                this.plugin,
                identifier,
                getOutfitItem(identifier),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                getLeggingsItem(identifier),
                getBootsItem(identifier)
        );
    }

    private Color getColor(String outfit, String s) {
        String color = this.config.getString("Outfits." + outfit + '.' + s + ".Color");
        String[] colors = color.split(",");
        if (colors.length != 3) {
            General.sendColoredMessage(this.plugin, "Misconfigured Color at Outfit " + outfit + ", " + s + '!', ChatColor.RED);
            return Color.BLACK;
        }
        int red = Parsing.parseInt(colors[0]);
        int green = Parsing.parseInt(colors[1]);
        int blue = Parsing.parseInt(colors[2]);
        return Color.fromRGB(red, green, blue);
    }

    public Iterable<OutfitStorage> getWardrobe() {
        return Collections.unmodifiableList(this.wardrobe);
    }

    public int getRemoveItemPosition() {
        return this.removeItem_Position;
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(OutfitStorage outfit) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", outfit.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public boolean isKeepOnDeath() {
        return this.keepOnDeath;
    }

    public boolean isCheckOnBody() {
        return this.checkOnBody;
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

    public int getMaxPage() {
        return this.maxPage;
    }
}
