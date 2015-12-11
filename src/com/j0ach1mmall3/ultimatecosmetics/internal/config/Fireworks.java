package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.FireworkStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 16:40 22/08/2015 using IntelliJ IDEA.
 */
public final class Fireworks extends ConfigLoader {
    private final Main plugin;
    private final boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final List<FireworkStorage> fireworks;

    private final int maxPage;

    public Fireworks(Main plugin) {
        super("fireworks.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.fireworks = getFireworksInternal();
        this.maxPage = getMaxPageInternal();
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Fireworks config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (FireworkStorage firework : this.fireworks) {
            if (firework.getPosition() > biggestSlot) biggestSlot = firework.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<FireworkStorage> getFireworksInternal() {
        return this.customConfig.getKeys("Fireworks").stream().map(this::getFireworkByIdentifier).collect(Collectors.toList());
    }

    private CustomItem getFireworkItem(String identifier) {
        String path = "Fireworks." + identifier + '.';
        CustomItem firework = new CustomItem(Material.FIREWORK, 1, 0, Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
        meta.setPower(Parsing.parseInt(this.config.getString(path + "Power")));
        FireworkEffect.Builder builder = FireworkEffect.builder();
        for (String color : this.config.getStringList(path + "Colors")) {
            builder.withColor(DyeColor.valueOf(color).getFireworkColor());
        }
        for (String color : this.config.getStringList(path + "FadeColors")) {
            builder.withFade(DyeColor.valueOf(color).getFireworkColor());
        }
        builder.with(FireworkEffect.Type.valueOf(this.config.getString(path + "Type")));
        builder.flicker(this.config.getBoolean(path + "Flicker"));
        builder.trail(this.config.getBoolean(path + "Trail"));
        meta.addEffect(builder.build());
        firework.setItemMeta(meta);
        return firework;
    }

    private FireworkStorage getFireworkByIdentifier(String identifier) {
        String path = "Fireworks." + identifier + '.';
        return new FireworkStorage(
                this.plugin,
                identifier,
                getFireworkItem(identifier),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission")
        );
    }

    public Iterable<FireworkStorage> getFireworks() {
        return Collections.unmodifiableList(this.fireworks);
    }

    public String getGuiName() {
        return this.guiName;
    }

    public int getGuiSize() {
        return this.guiSize;
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

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(FireworkStorage firework) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", firework.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
