package com.j0ach1mmall3.ultimatecosmetics.internal.pets;

import org.bukkit.entity.EntityType;

public enum PetType {
    CREEPER("Creeper", EntityType.CREEPER, 50),
    SKELETON("Skeleton", EntityType.SKELETON, 51),
    SPIDER("Spider", EntityType.SPIDER, 52),
    GIANT("Giant", EntityType.GIANT, 53),
    ZOMBIE("Zombie", EntityType.ZOMBIE, 54),
    PIG_ZOMBIE("PigZombie", EntityType.PIG_ZOMBIE, 57),
    ENDERMAN("Enderman", EntityType.ENDERMAN, 58),
    CAVE_SPIDER("CaveSpider", EntityType.CAVE_SPIDER, 59),
    SILVERFISH("Silverfish", EntityType.SILVERFISH, 60),
    BLAZE("Blaze", EntityType.BLAZE, 61),
    WITCH("Witch", EntityType.WITCH, 66),
    ENDERMITE("Endermite", EntityType.ENDERMITE, 67),
    PIG("Pig", EntityType.PIG, 90),
    SHEEP("Sheep", EntityType.SHEEP, 91),
    COW("Cow", EntityType.COW, 92),
    CHICKEN("Chicken", EntityType.CHICKEN, 93),
    SQUID("Squid", EntityType.SQUID, 94),
    WOLF("Wolf", EntityType.WOLF, 95),
    MUSHROOM_COW("MushroomCow", EntityType.MUSHROOM_COW, 96),
    SNOWMAN("SnowMan", EntityType.SNOWMAN, 97),
    OCELOT("Ocelot", EntityType.OCELOT, 98),
    IRON_GOLEM("IronGolem", EntityType.IRON_GOLEM, 99),
    HORSE("Horse", EntityType.HORSE, 100),
    RABBIT("Rabbit", EntityType.RABBIT, 101),
    VILLAGER("Villager", EntityType.VILLAGER, 120);
    private final String name;
    private final EntityType type;
    private final int id;

    PetType(String name, EntityType type, int id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EntityType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }
}
