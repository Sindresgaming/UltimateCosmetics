package com.j0ach1mmall3.ultimatecosmetics.internal.pets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum PetData {
    BABY("baby", PetData.Type.BOOLEAN),
    BLACK("black", PetData.Type.COLOR, PetData.Type.CAT, PetData.Type.HORSE_COLOR, PetData.Type.RABBIT),
    BLACK_AND_WHITE("blackAndWhite", PetData.Type.RABBIT),
    BLACKSMITH("blacksmith", PetData.Type.PROFESSION),
    BLACK_DOTS("blackdots", PetData.Type.HORSE_STYLE),
    BLUE("blue", PetData.Type.COLOR),
    BROWN("brown", PetData.Type.COLOR, PetData.Type.HORSE_COLOR, PetData.Type.RABBIT),
    BUTCHER("butcher", PetData.Type.PROFESSION),
    CHESTED("chested", PetData.Type.BOOLEAN),
    CHESTNUT("chestnut", PetData.Type.HORSE_COLOR),
    CREAMY("creamy", PetData.Type.HORSE_COLOR),
    CYAN("cyan", PetData.Type.COLOR),
    DARK_BROWN("darkBrown", PetData.Type.HORSE_COLOR),
    DIAMOND("diamond", PetData.Type.HORSE_ARMOUR),
    DONKEY("donkey", PetData.Type.HORSE_VARIANT),
    FARMER("farmer", PetData.Type.PROFESSION),
    FIRE("fire", PetData.Type.BOOLEAN),
    GRAY("gray", PetData.Type.COLOR, PetData.Type.HORSE_COLOR),
    GREEN("green", PetData.Type.COLOR),
    GOLD("gold", PetData.Type.HORSE_ARMOUR, PetData.Type.RABBIT),
    HORSE("horse", PetData.Type.HORSE_VARIANT),
    IRON("iron", PetData.Type.HORSE_ARMOUR),
    LIBRARIAN("librarian", PetData.Type.PROFESSION),
    LIGHT_BLUE("lightBlue", PetData.Type.COLOR),
    LIME("lime", PetData.Type.COLOR),
    MAGENTA("magenta", PetData.Type.COLOR),
    MULE("mule", PetData.Type.HORSE_VARIANT),
    NONE("none", PetData.Type.HORSE_STYLE),
    ORANGE("orange", PetData.Type.COLOR),
    PINK("pink", PetData.Type.COLOR),
    POWER("powered", PetData.Type.BOOLEAN),
    PRIEST("priest", PetData.Type.PROFESSION),
    PURPLE("purple", PetData.Type.COLOR),
    RED("red", PetData.Type.CAT, PetData.Type.COLOR),
    SADDLE("saddle", PetData.Type.BOOLEAN),
    SALT_AND_PEPPER("saltAndPepper", PetData.Type.RABBIT),
    SHEARED("sheared", PetData.Type.BOOLEAN),
    SIAMESE("siamese", PetData.Type.CAT),
    SILVER("silver", PetData.Type.COLOR),
    SKELETON_HORSE("skeletonHorse", PetData.Type.HORSE_VARIANT),
    TAMED("tamed", PetData.Type.BOOLEAN),
    THE_KILLER_BUNNY("killer", PetData.Type.RABBIT),
    VILLAGER("villager", PetData.Type.BOOLEAN),
    WHITEFIELD("whitepatch", PetData.Type.HORSE_STYLE),
    WHITE_DOTS("whiteDots", PetData.Type.HORSE_STYLE),
    WHITE("white", PetData.Type.COLOR, PetData.Type.HORSE_COLOR, PetData.Type.HORSE_STYLE, PetData.Type.RABBIT),
    WILD("wild", PetData.Type.CAT),
    WITHER("wither", PetData.Type.BOOLEAN),
    YELLOW("yellow", PetData.Type.COLOR),
    UNDEAD_HORSE("zombieHorse", PetData.Type.HORSE_VARIANT);
    private final List<PetData.Type> t;

    PetData(String name, PetData.Type... t) {
        this.t = Collections.unmodifiableList(Arrays.asList(t));
    }

    public Collection<PetData.Type> getTypes() {
        return Collections.unmodifiableList(this.t);
    }

    boolean isType(PetData.Type t) {
        return this.t.contains(t);
    }

    @SuppressWarnings("PublicInnerClass")
    public enum Type {
        BOOLEAN, COLOR, CAT, PROFESSION, HORSE_ARMOUR, HORSE_COLOR, HORSE_VARIANT, HORSE_STYLE, RABBIT
    }
}
