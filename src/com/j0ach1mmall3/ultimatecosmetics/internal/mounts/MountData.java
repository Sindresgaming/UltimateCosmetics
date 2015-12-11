package com.j0ach1mmall3.ultimatecosmetics.internal.mounts;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum MountData {
    BABY("baby", MountData.Type.BOOLEAN),
    BLACK("black", MountData.Type.COLOR, MountData.Type.CAT, MountData.Type.HORSE_COLOR, MountData.Type.RABBIT),
    BLACK_AND_WHITE("blackAndWhite", MountData.Type.RABBIT),
    BLACKSMITH("blacksmith", MountData.Type.PROFESSION),
    BLACK_DOTS("blackdots", MountData.Type.HORSE_STYLE),
    BLUE("blue", MountData.Type.COLOR),
    BROWN("brown", MountData.Type.COLOR, MountData.Type.HORSE_COLOR, MountData.Type.RABBIT),
    BUTCHER("butcher", MountData.Type.PROFESSION),
    CHESTED("chested", MountData.Type.BOOLEAN),
    CHESTNUT("chestnut", MountData.Type.HORSE_COLOR),
    CREAMY("creamy", MountData.Type.HORSE_COLOR),
    CYAN("cyan", MountData.Type.COLOR),
    DARK_BROWN("darkBrown", MountData.Type.HORSE_COLOR),
    DIAMOND("diamond", MountData.Type.HORSE_ARMOUR),
    DONKEY("donkey", MountData.Type.HORSE_VARIANT),
    FARMER("farmer", MountData.Type.PROFESSION),
    FIRE("fire", MountData.Type.BOOLEAN),
    GRAY("gray", MountData.Type.COLOR, MountData.Type.HORSE_COLOR),
    GREEN("green", MountData.Type.COLOR),
    GOLD("gold", MountData.Type.HORSE_ARMOUR, MountData.Type.RABBIT),
    HORSE("horse", MountData.Type.HORSE_VARIANT),
    IRON("iron", MountData.Type.HORSE_ARMOUR),
    LIBRARIAN("librarian", MountData.Type.PROFESSION),
    LIGHT_BLUE("lightBlue", MountData.Type.COLOR),
    LIME("lime", MountData.Type.COLOR),
    MAGENTA("magenta", MountData.Type.COLOR),
    MULE("mule", MountData.Type.HORSE_VARIANT),
    NONE("none", MountData.Type.HORSE_STYLE),
    ORANGE("orange", MountData.Type.COLOR),
    PINK("pink", MountData.Type.COLOR),
    POWER("powered", MountData.Type.BOOLEAN),
    PRIEST("priest", MountData.Type.PROFESSION),
    PURPLE("purple", MountData.Type.COLOR),
    RED("red", MountData.Type.CAT, MountData.Type.COLOR),
    SADDLE("saddle", MountData.Type.BOOLEAN),
    SALT_AND_PEPPER("saltAndPepper", MountData.Type.RABBIT),
    SHEARED("sheared", MountData.Type.BOOLEAN),
    SIAMESE("siamese", MountData.Type.CAT),
    SILVER("silver", MountData.Type.COLOR),
    SKELETON_HORSE("skeletonHorse", MountData.Type.HORSE_VARIANT),
    TAMED("tamed", MountData.Type.BOOLEAN),
    THE_KILLER_BUNNY("killer", MountData.Type.RABBIT),
    VILLAGER("villager", MountData.Type.BOOLEAN),
    WHITEFIELD("whitepatch", MountData.Type.HORSE_STYLE),
    WHITE_DOTS("whiteDots", MountData.Type.HORSE_STYLE),
    WHITE("white", MountData.Type.COLOR, MountData.Type.HORSE_COLOR, MountData.Type.HORSE_STYLE, MountData.Type.RABBIT),
    WILD("wild", MountData.Type.CAT),
    WITHER("wither", MountData.Type.BOOLEAN),
    YELLOW("yellow", MountData.Type.COLOR),
    UNDEAD_HORSE("zombieHorse", MountData.Type.HORSE_VARIANT);
    private final List<MountData.Type> t;

    MountData(String name, MountData.Type... t) {
        this.t = ImmutableList.copyOf(t);
    }

    public Collection<MountData.Type> getTypes() {
        return Collections.unmodifiableList(this.t);
    }

    boolean isType(MountData.Type t) {
        return this.t.contains(t);
    }

    @SuppressWarnings("PublicInnerClass")
    public enum Type {
        BOOLEAN, COLOR, CAT, PROFESSION, HORSE_ARMOUR, HORSE_COLOR, HORSE_VARIANT, HORSE_STYLE, RABBIT
    }
}
