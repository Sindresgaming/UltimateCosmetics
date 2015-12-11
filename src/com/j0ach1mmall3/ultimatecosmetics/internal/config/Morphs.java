package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.MorphStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 20:42 1/10/2015 using IntelliJ IDEA.
 */
public final class Morphs extends ConfigLoader {
    private final Main plugin;
    private boolean enabled;
    private final String command;
    private final String noPermissionMessage;
    private final Sound giveSound;
    private final Sound removeSound;
    private final boolean enableAbilities;
    private final boolean checkInSlot;
    private final int abilitySlot;
    private final String guiName;
    private final int guiSize;
    private final CustomItem noPermissionItem;
    private final boolean noPermissionItem_Enabled;
    private final CustomItem removeItem;
    private final int removeItem_Position;
    private List<MorphStorage> morphs = null;

    private int maxPage;

    public Morphs(Main plugin) {
        super("morphs.yml", plugin);
        this.plugin = plugin;
        Config pluginConfig = plugin.getBabies();
        this.enabled = this.config.getBoolean("Enabled");
        this.command = this.config.getString("Command");
        this.noPermissionMessage = this.config.getString("NoPermissionMessage");
        this.giveSound = Sound.valueOf(this.config.getString("GiveSound"));
        this.removeSound = Sound.valueOf(this.config.getString("RemoveSound"));
        this.enableAbilities = this.config.getBoolean("EnableAbilities");
        this.checkInSlot = this.config.getBoolean("CheckInSlot");
        this.abilitySlot = this.config.getInt("AbilitySlot");
        this.guiName = this.config.getString("GUIName");
        this.guiSize = Parsing.parseInt(this.config.getString("GUISize"));
        this.noPermissionItem = Methods.getNoPermissionItem(this.config);
        this.noPermissionItem_Enabled = this.config.getBoolean("NoPermissionItem.Enabled");
        this.removeItem = Methods.getRemoveItem(this.config);
        this.removeItem_Position = Parsing.parseInt(this.config.getString("RemoveItem.Position"));
        if (this.enabled && !plugin.isLibsDisguises()) {
            if (pluginConfig.getLoggingLevel() >= 1)
                General.sendColoredMessage(plugin, "Morphs are enabled in the Morphs config, but LibsDisguises isn't installed. Adjusting that value.", ChatColor.RED);
            this.enabled = false;
        }
        if (this.enabled) {
            this.morphs = getMorphsInternal();
            this.maxPage = getMaxPageInternal();
        }
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Morphs config successfully loaded!", ChatColor.GREEN);
    }

    private int getMaxPageInternal() {
        int biggestSlot = 0;
        for (MorphStorage morph : this.morphs) {
            if (morph.getPosition() > biggestSlot) biggestSlot = morph.getPosition();
        }
        return biggestSlot / 44;
    }

    private List<MorphStorage> getMorphsInternal() {
        return this.customConfig.getKeys("Morphs").stream().map(this::getMorphByIdentifier).collect(Collectors.toList());
    }

    private CustomItem getMorphItem(String identifier) {
        String path = "Morphs." + identifier + '.';
        CustomItem morph = new CustomItem(Material.SKULL_ITEM, 1, 3, Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
        SkullMeta meta = (SkullMeta) morph.getItemMeta();
        meta.setOwner(getOwner(this.config.getString(path + "MorphType")));
        morph.setItemMeta(meta);
        return morph;
    }

    private CustomItem getAbilityItem(String identifier) {
        String path = "Morphs." + identifier + ".Ability.";
        return new CustomItem(Parsing.parseMaterial(this.config.getString(path + "Item")), 1, Parsing.parseData(this.config.getString(path + "Item")), Placeholders.parse(this.config.getString(path + "Name")), Placeholders.parse(this.config.getString(path + "Description")));
    }

    private MorphStorage getMorphByIdentifier(String identifier) {
        String path = "Morphs." + identifier + '.';
        return new MorphStorage(
                this.plugin,
                identifier,
                getMorphItem(identifier),
                Parsing.parseInt(this.config.getString(path + "Position")),
                this.config.getString(path + "Permission"),
                this.config.getString(path + "MorphType"),
                getAbilityItem(identifier),
                this.config.getInt(path + "Ability.Duration"),
                this.config.getInt(path + "Ability.Cooldown")
        );
    }

    public MorphStorage getMorphByAbilityItem(ItemStack item) {
        for (MorphStorage morph : this.morphs) {
            if (morph.getAbilityItem().isSimilar(item)) return morph;
        }
        return null;
    }

    public boolean isAbilityItem(ItemStack item) {
        for (MorphStorage morph : this.morphs) {
            if (morph.getAbilityItem().isSimilar(item)) return true;
        }
        return false;
    }

    private String getOwner(String type) {
        switch (type) {
            case "BAT":
                return "bozzobrain";
            case "BLAZE":
                return "MHF_Blaze";
            case "CAVE_SPIDER":
                return "MHF_CaveSpider";
            case "CHICKEN":
                return "MHF_Chicken";
            case "COW":
                return "MHF_Cow";
            case "CREEPER":
                return "MHF_Creeper";
            case "DONKEY":
                return "gavertoso";
            case "ELDER_GUARDIAN":
                return "MHF_EGuardian";
            case "ENDER_DRAGON":
                return "MHF_EnderDragon";
            case "ENDERMAN":
                return "MHF_Enderman";
            case "ENDERMITE":
                return "MHF_Endermite";
            case "GHAST":
                return "MHF_Ghast";
            case "GIANT":
                return "MHF_Zombie";
            case "GUARDIAN":
                return "MHF_Guardian";
            case "HORSE":
                return "gavertoso";
            case "IRON_GOLEM":
                return "MHF_Golem";
            case "MAGMA_CUBE":
                return "MHF_LavaSlime";
            case "MULE":
                return "gavertoso";
            case "MUSHROOM_COW":
                return "MHF_MushroomCow";
            case "OCELOT":
                return "MHF_Ocelot";
            case "PIG":
                return "MHF_Pig";
            case "PIG_ZOMBIE":
                return "MHF_PigZombie";
            case "RABBIT":
                return "Natalieisawesome";
            case "SHEEP":
                return "MHF_Sheep";
            case "SILVERFISH":
                return "AlexVMiner";
            case "SKELETON":
                return "MHF_Skeleton";
            case "SKELETON_HORSE":
                return "MHF_Skeleton";
            case "SLIME":
                return "MHF_Slime";
            case "SNOWMAN":
                return "Koebasti";
            case "SPIDER":
                return "MHF_Spider";
            case "SQUID":
                return "MHF_Squid";
            case "UNDEAD_HORSE":
                return "MHF_Zombie";
            case "VILLAGER":
                return "MHF_Villager";
            case "WITCH":
                return "MHF_Witch";
            case "WITHER":
                return "MHF_Wither";
            case "WITHER_SKELETON":
                return "MHF_WSkeleton";
            case "WOLF":
                return "MHF_Wolf";
            case "ZOMBIE":
                return "MHF_Zombie";
            case "ZOMBIE_VILLAGER":
                return "MHF_Zombie";
        }
        return "MHF_Steve";
    }

    public Main getPlugin() {
        return this.plugin;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getCommand() {
        return this.command;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }

    public Sound getGiveSound() {
        return this.giveSound;
    }

    public Sound getRemoveSound() {
        return this.removeSound;
    }

    public boolean isEnableAbilities() {
        return this.enableAbilities;
    }

    public boolean isCheckInSlot() {
        return this.checkInSlot;
    }

    public int getAbilitySlot() {
        return this.abilitySlot;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public int getGuiSize() {
        return this.guiSize;
    }

    public CustomItem getNoPermissionItem() {
        return this.noPermissionItem;
    }

    public CustomItem getNoPermissionItem(MorphStorage morph) {
        if ("%cosmeticsname%".equals(this.noPermissionItem.getItemMeta().getDisplayName())) {
            CustomItem item = new CustomItem(this.noPermissionItem.clone());
            item.setName(item.getItemMeta().getDisplayName().replace("%cosmeticsname%", morph.getItem().getItemMeta().getDisplayName()));
            return item;
        }
        return this.noPermissionItem;
    }

    public boolean isNoPermissionItemEnabled() {
        return this.noPermissionItem_Enabled;
    }

    public CustomItem getRemoveItem() {
        return this.removeItem;
    }

    public int getRemoveItemPosition() {
        return this.removeItem_Position;
    }

    public Iterable<MorphStorage> getMorphs() {
        return Collections.unmodifiableList(this.morphs);
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
