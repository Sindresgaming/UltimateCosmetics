package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.internal.storage.DoubleJumpStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by j0ach1mmall3 on 11:28 23/08/2015 using IntelliJ IDEA.
 */
public final class Misc extends ConfigLoader {
    private final boolean stacker_Enabled;
    private final String stacker_Permission;
    private final String stacker_NoPermissionMessage;
    private final boolean stacker_StackPlayersOnly;
    private final String stacker_Prefix;
    private final boolean doubleJump_Enabled;
    private final List<DoubleJumpStorage> doubleJumpGroups;

    public Misc(Main plugin) {
        super("misc.yml", plugin);
        this.stacker_Enabled = this.config.getBoolean("Stacker.Enabled");
        this.stacker_Permission = this.config.getString("Stacker.Permission");
        this.stacker_NoPermissionMessage = this.config.getString("Stacker.NoPermissionMessage");
        this.stacker_StackPlayersOnly = this.config.getBoolean("Stacker.StackPlayersOnly");
        this.stacker_Prefix = this.config.getString("Stacker.Prefix");
        this.doubleJump_Enabled = this.config.getBoolean("DoubleJump.Enabled");
        this.doubleJumpGroups = getDoubleJumpGroupsInternal();
    }

    private List<DoubleJumpStorage> getDoubleJumpGroupsInternal() {
        return this.customConfig.getKeys("DoubleJump.Groups").stream().map(this::getDoubleJumpGroupByIdentifier).collect(Collectors.toList());
    }

    private DoubleJumpStorage getDoubleJumpGroupByIdentifier(String identifier) {
        String path = "DoubleJump.Groups." + identifier + '.';
        return new DoubleJumpStorage(
                identifier,
                Parsing.parseInt(this.config.getString(path + "Multiplier")),
                this.config.getString(path + "Permission")
        );
    }

    public boolean isStackerEnabled() {
        return this.stacker_Enabled;
    }

    public Iterable<DoubleJumpStorage> getDoubleJumpGroups() {
        return Collections.unmodifiableList(this.doubleJumpGroups);
    }

    public boolean isDoubleJumpEnabled() {
        return this.doubleJump_Enabled;
    }

    public String getStackerPrefix() {
        return this.stacker_Prefix;
    }

    public String getStackerNoPermissionMessage() {
        return this.stacker_NoPermissionMessage;
    }

    public boolean isStackerStackPlayersOnly() {
        return this.stacker_StackPlayersOnly;
    }

    public String getStackerPermission() {
        return this.stacker_Permission;
    }
}
