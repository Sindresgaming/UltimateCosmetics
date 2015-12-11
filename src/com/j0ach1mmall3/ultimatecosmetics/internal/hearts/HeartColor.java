package com.j0ach1mmall3.ultimatecosmetics.internal.hearts;

/**
 * Created by j0ach1mmall3 on 4:36 23/08/2015 using IntelliJ IDEA.
 */
public enum HeartColor {
    RED("None", 0),
    GREEN("POISON", 1),
    BLACK("WITHER", 2),
    YELLOW("ABSORPTION", 3);
    private final String potioneffect;
    private final int id;

    HeartColor(String potioneffect, int id) {
        this.potioneffect = potioneffect;
        this.id = id;
    }

    public String getPotionEffect() {
        return this.potioneffect;
    }

    public int getId() {
        return this.id;
    }
}
