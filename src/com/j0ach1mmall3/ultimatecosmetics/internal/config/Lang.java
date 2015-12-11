package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;

/**
 * Created by j0ach1mmall3 on 9:54 21/08/2015 using IntelliJ IDEA.
 */
public final class Lang extends ConfigLoader {
    private final String notEnabled;
    private final String commandNoPermission;
    private final String alreadyOnHead;
    private final String dismountVehicle;
    private final String notEnoughSpace;
    private final String songNotFound;
    private final String renamePet;
    private final String successfulRename;
    private final String alreadyOnBody;
    private final String stackedPlayer;
    private final String stackedByPlayer;
    private final String gadgetsCooldown;
    private final String lostAmmo;
    private final String notEnoughAmmo;
    private final String alreadyInGadgetsSlot;
    private final String alreadyInAbilitySlot;
    private final String abilityCooldown;
    private final String toggledStacker;
    private final String stackerNotEnabled;
    private final String stackedNotEnabled;

    public Lang(Main plugin) {
        super("lang.yml", plugin);
        Config pluginConfig = plugin.getBabies();
        this.notEnabled = this.config.getString("NotEnabled");
        this.commandNoPermission = this.config.getString("CommandNoPermission");
        this.alreadyOnHead = this.config.getString("AlreadyOnHead");
        this.dismountVehicle = this.config.getString("DismountVehicle");
        this.notEnoughSpace = this.config.getString("NotEnoughSpace");
        this.songNotFound = this.config.getString("SongNotFound");
        this.renamePet = this.config.getString("RenamePet");
        this.successfulRename = this.config.getString("SuccessfulRename");
        this.alreadyOnBody = this.config.getString("AlreadyOnBody");
        this.stackedPlayer = this.config.getString("StackedPlayer");
        this.stackedByPlayer = this.config.getString("StackedByPlayer");
        this.gadgetsCooldown = this.config.getString("GadgetsCooldown");
        this.lostAmmo = this.config.getString("LostAmmo");
        this.notEnoughAmmo = this.config.getString("NotEnoughAmmo");
        this.alreadyInGadgetsSlot = this.config.getString("AlreadyInGadgetsSlot");
        this.alreadyInAbilitySlot = this.config.getString("AlreadyInAbilitySlot");
        this.abilityCooldown = this.config.getString("AbilityCooldown");
        this.toggledStacker = this.config.getString("ToggledStacker");
        this.stackerNotEnabled = this.config.getString("StackerNotEnabled");
        this.stackedNotEnabled = this.config.getString("StackedNotEnabled");
        if (pluginConfig.getLoggingLevel() >= 2)
            General.sendColoredMessage(plugin, "Language config successfully loaded!", ChatColor.GREEN);

    }

    public String getStackedByPlayer() {
        return this.stackedByPlayer;
    }

    public String getStackedPlayer() {
        return this.stackedPlayer;
    }

    public String getAlreadyOnBody() {
        return this.alreadyOnBody;
    }

    public String getSuccessfulRename() {
        return this.successfulRename;
    }

    public String getRenamePet() {
        return this.renamePet;
    }

    public String getSongNotFound() {
        return this.songNotFound;
    }

    public String getNotEnoughSpace() {
        return this.notEnoughSpace;
    }

    public String getDismountVehicle() {
        return this.dismountVehicle;
    }

    public String getAlreadyOnHead() {
        return this.alreadyOnHead;
    }

    public String getCommandNoPermission() {
        return this.commandNoPermission;
    }

    public String getNotEnabled() {
        return this.notEnabled;
    }

    public String getGadgetsCooldown() {
        return this.gadgetsCooldown;
    }

    public String getNotEnoughAmmo() {
        return this.notEnoughAmmo;
    }

    public String getLostAmmo() {
        return this.lostAmmo;
    }

    public String getAlreadyInGadgetsSlot() {
        return this.alreadyInGadgetsSlot;
    }

    public String getAlreadyInAbilitySlot() {
        return this.alreadyInAbilitySlot;
    }

    public String getAbilityCooldown() {
        return this.abilityCooldown;
    }

    public String getToggledStacker() {
        return this.toggledStacker;
    }

    public String getStackerNotEnabled() {
        return this.stackerNotEnabled;
    }

    public String getStackedNotEnabled() {
        return this.stackedNotEnabled;
    }
}
