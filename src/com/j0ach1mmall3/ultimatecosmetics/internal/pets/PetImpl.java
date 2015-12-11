package com.j0ach1mmall3.ultimatecosmetics.internal.pets;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Pet;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.PetStorage;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

/**
 * Created by j0ach1mmall3 on 15:06 23/08/2015 using IntelliJ IDEA.
 */
public final class PetImpl implements Pet {
    private Player player;
    private Creature ent = null;
    private PetStorage petStorage;

    public PetImpl(Player player, PetStorage petStorage) {
        this.player = player;
        this.petStorage = petStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Creature getEntity() {
        return this.ent;
    }

    @Override
    public PetStorage getPetStorage() {
        return this.petStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.petStorage.getPlugin().getApi();
        if (api.hasPet(this.player)) {
            api.getPet(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Pet cosmetic = (Pet) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.petStorage = cosmetic.getPetStorage();
            this.ent = cosmetic.getEntity();
            PetType petType = PetType.valueOf(this.petStorage.getPetType());
            Creature entity = (Creature) this.player.getWorld().spawnEntity(this.player.getLocation(), petType.getType());
            PetReflection.removeGoalSelectors(entity);
            PetReflection.addGoalSelectors(entity);
            entity.teleport(this.player);
            entity.setTarget(this.player);
            entity.setCanPickupItems(false);
            for (String petData : this.petStorage.getPetData()) {
                addPetData(entity, PetData.valueOf(petData));
            }
            entity.setCustomName(Placeholders.parse(this.petStorage.getItem().getItemMeta().getDisplayName(), this.player));
            entity.setCustomNameVisible(true);
            entity.setMaxHealth(20.0D);
            entity.setHealth(entity.getMaxHealth());
            this.ent = entity;
            api.addPet(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.petStorage.getPlugin().getApi();
        if (api.hasPet(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.ent = ((Pet) cosmetic).getEntity();
            api.removePet(this);
            this.ent.remove();
        }
    }

    private void addPetData(Entity ent, PetData data) {
        if (ent instanceof Ageable) {
            ((Ageable) ent).setBreed(false);
        }
        if (data.getTypes().contains(PetData.Type.COLOR)) {
            if (ent instanceof Colorable) {
                ((Colorable) ent).setColor(DyeColor.valueOf(data.name()));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.CAT)) {
            if (ent instanceof Ocelot) {
                ((Ocelot) ent).setCatType(Ocelot.Type.valueOf(data.name() + "_CAT"));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.PROFESSION)) {
            if (ent instanceof Villager) {
                ((Villager) ent).setProfession(Villager.Profession.valueOf(data.name()));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.HORSE_ARMOUR)) {
            if (ent instanceof Horse) {
                ((Horse) ent).getInventory().setArmor(new ItemStack(Material.valueOf(data + "_BARDING")));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.HORSE_COLOR)) {
            if (ent instanceof Horse) {
                ((Horse) ent).setColor(Horse.Color.valueOf(data.name()));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.HORSE_VARIANT)) {
            if (ent instanceof Horse) {
                ((Horse) ent).setVariant(Horse.Variant.valueOf(data.name()));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.HORSE_STYLE)) {
            if (ent instanceof Horse) {
                ((Horse) ent).setStyle(Horse.Style.valueOf(data.name()));
                return;
            }
        }
        if (data.getTypes().contains(PetData.Type.RABBIT)) {
            if (ReflectionAPI.verBiggerThan(1, 8)) {
                if (ent instanceof org.bukkit.entity.Rabbit) {
                    ((org.bukkit.entity.Rabbit) ent).setRabbitType(org.bukkit.entity.Rabbit.Type.valueOf(data.name()));
                    return;
                }
            }
        }
        switch (data) {
            case BABY:
                //noinspection ChainOfInstanceofChecks
                if (ent instanceof Ageable) {
                    ((Ageable) ent).setBaby();
                    ((Ageable) ent).setAgeLock(true);
                }
                if (ent instanceof Zombie) ((Zombie) ent).setBaby(true);
                return;
            case FIRE:
                ent.setFireTicks(Integer.MAX_VALUE);
                return;
            case CHESTED:
                if (ent instanceof Horse) {
                    ((Horse) ent).setCarryingChest(true);
                }
                return;
            case POWER:
                if (ent instanceof Creeper) {
                    ((Creeper) ent).setPowered(true);
                }
                return;
            case SADDLE:
                if (ent instanceof Pig) {
                    ((Pig) ent).setSaddle(true);
                }
                return;
            case SHEARED:
                if (ent instanceof Sheep) {
                    ((Sheep) ent).setSheared(true);
                }
                return;
            case TAMED:
                if (ent instanceof Tameable) {
                    ((Tameable) ent).setTamed(true);
                }
                return;
            case VILLAGER:
                if (ent instanceof Zombie) {
                    ((Zombie) ent).setVillager(true);
                }
                return;
            case WITHER:
                if (ent instanceof Skeleton) {
                    ((Skeleton) ent).setSkeletonType(Skeleton.SkeletonType.WITHER);
                }
        }
    }
}
