package com.j0ach1mmall3.ultimatecosmetics.internal.storage;

/**
 * Created by j0ach1mmall3 on 11:45 23/08/2015 using IntelliJ IDEA.
 */
public final class DoubleJumpStorage {
    private String identifier;
    private String permission;
    private int multiplier;

    public DoubleJumpStorage(String identifier, int multiplier, String permission) {
        this.permission = permission;
        this.multiplier = multiplier;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
