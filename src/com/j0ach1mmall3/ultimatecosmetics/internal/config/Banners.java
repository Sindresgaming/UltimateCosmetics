package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BannerStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 4:56 21/08/2015 using IntelliJ IDEA.
 */
public final class Banners extends ConfigLoader {
    private final Main plugin;
    private boolean enabled;
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
    private List<BannerStorage> banners = null;

    private int maxPage;

    public Banners(Main plugin) {
        super("banners.yml", plugin);
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
        if (this.enabled && !ReflectionAPI.verBiggerThan(1, 8)) {
            if (pluginConfig.getLoggingLevel() >= 1)
                General.sendColoredMessage(plugin, "Banners are enabled in the Banners config, but you are running 1.7 or lower. Adjusting that value.", ChatColor.RED);
            this.enabled = false;
        }
        if (this.enabled) {
            this.banners = getBannersInternal();
            this.maxPage = getMaxPageInternal();
        }
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Banners config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (BannerStorage banner : this.banners) {
            if (banner.getPosition() > biggestSlot) biggestSlot = banner.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<BannerStorage> getBannersInternal() {
        return this.customConfig.getKeys("Banners").stream().map(this::getBannerByIdentifier).collect(Collectors.toList());
    }

    private CustomItem getBannerItem(String identifier) {
        String path = "Banners." + identifier + '.';
        CustomItem banner = new CustomItem(Material.BANNER, 1, 0, Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        meta.setBaseColor(DyeColor.valueOf(this.config.getString(path + "Color")));
        for (String s : this.customConfig.getKeys(path + "Patterns")) {
            meta.addPattern(new Pattern(DyeColor.valueOf(this.config.getString(path + "Patterns." + s + ".Color")), PatternType.valueOf(this.config.getString(path + "Patterns." + s + ".PatternType"))));
        }
        banner.setItemMeta(meta);
        return banner;
    }

    private BannerStorage getBannerByIdentifier(String identifier) {
        String path = "Banners." + identifier + '.';
        return new BannerStorage(
                this.plugin,
                identifier,
                getBannerItem(identifier),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission")
        );
    }

    public Iterable<BannerStorage> getBanners() {
        return Collections.unmodifiableList(this.banners);
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

    public CustomItem getNoPermissionItem(BannerStorage banner) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", banner.getItem().getItemMeta().getDisplayName()));
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
