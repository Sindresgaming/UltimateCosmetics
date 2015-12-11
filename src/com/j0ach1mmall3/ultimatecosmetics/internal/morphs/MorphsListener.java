package com.j0ach1mmall3.ultimatecosmetics.internal.morphs;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.Parsing;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.MorphStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Morphs;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by j0ach1mmall3 on 1:00 2/10/2015 using IntelliJ IDEA.
 */
public final class MorphsListener implements Listener {
    private final Main plugin;
    private final Collection<Entity> entitiesQueue = new ArrayList<>();
    private final HashMap<String, String> cooldownPlayers = new HashMap<>();

    public MorphsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            double damage = e.getDamage();
            if (damage >= p.getHealth()) {
                if (this.plugin.getApi().hasMorph(p) && this.plugin.getMorphs().isEnableAbilities())
                    p.getInventory().setItem(this.plugin.getMorphs().getAbilitySlot(), null);
            }
        }
        if (e.getEntity() instanceof LivingEntity && isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("AbilityArrow") || e.getDamager().hasMetadata("AbilitySnowball") || e.getDamager().hasMetadata("AbilitySkull")) {
            e.setCancelled(true);
            e.getDamager().remove();
            this.entitiesQueue.remove(e.getDamager());
            return;
        }
        if (e.getEntity() instanceof LivingEntity && isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity().hasMetadata("AbilitySkull")) {
            e.setCancelled(true);
            e.getEntity().remove();
            this.entitiesQueue.remove(e.getEntity());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (this.plugin.isLibsDisguises() && this.plugin.getMorphs().isEnabled() && this.plugin.getMorphs().isAbilityItem(e.getItemDrop().getItemStack()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.plugin.isLibsDisguises() && this.plugin.getMorphs().isEnabled() && this.plugin.getMorphs().isAbilityItem(e.getCurrentItem()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("AbilityEgg")) {
            this.entitiesQueue.remove(e.getEntity());
            e.getEntity().remove();
            int duration = e.getEntity().getMetadata("AbilityEgg").get(0).asInt();
            Ageable chicken = (Ageable) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.CHICKEN);
            chicken.setBaby();
            chicken.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, 1));
            this.entitiesQueue.add(chicken);
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                this.entitiesQueue.remove(chicken);
                chicken.remove();
            }, 20 * duration);
            return;
        }
        if (e.getEntity().hasMetadata("AbilityArrow") || e.getEntity().hasMetadata("AbilitySnowball") || e.getEntity().hasMetadata("AbilitySkull")) {
            this.entitiesQueue.remove(e.getEntity());
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("AbilityItem")) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            e.getItem().remove();
            String metadata = e.getItem().getMetadata("AbilityItem").get(0).asString();
            if (metadata.contains("cobweb-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, 1));
                return;
            }
            if (metadata.contains("milkbucket-")) {
                for (PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
                return;
            }
            if (metadata.contains("ironingot-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 1));
                return;
            }
            if (metadata.contains("mushroom-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration * 20, 1));
                return;
            }
            if (metadata.contains("porkchop-")) {
                int duration = Parsing.parseInt(metadata.split("-")[1]);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, 1));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Morphs config = this.plugin.getMorphs();
        Lang lang = this.plugin.getLang();
        if (this.plugin.isLibsDisguises() && config.isAbilityItem(p.getItemInHand())) {
            MorphStorage morph = this.plugin.getMorphs().getMorphByAbilityItem(p.getItemInHand());
            if (e.getAction() != Action.PHYSICAL) e.setCancelled(true);
            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(morph.getIdentifier())) {
                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getAbilityCooldown().replace("%timeleft%", String.valueOf(morph.getAbilityCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                p.updateInventory();
                return;
            }
            this.cooldownPlayers.put(p.getName(), morph.getIdentifier() + ':' + System.currentTimeMillis());
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * morph.getAbilityCooldown());
            String morphType = morph.getMorphType().toLowerCase();
            if ("bat".equals(morphType) || "blaze".equals(morphType) || "ghast".equals(morphType) || "ender_dragon".equals(morphType)) {
                p.setAllowFlight(true);
                p.setFlying(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                    p.setAllowFlight(false);
                    p.setFlying(false);
                }, 20 * morph.getAbilityDuration());
                p.updateInventory();
                return;
            }
            if (morphType.contains("spider")) {
                throwItems(p, new CustomItem(Material.WEB, 1, 0), morph.getAbilityDuration(), "cobweb");
                return;
            }
            if ("chicken".equals(morphType)) {
                for (int i = 0; i < 10; i++) {
                    Vector velocity = p.getEyeLocation().getDirection();
                    velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                    velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                    velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                    Egg egg = p.launchProjectile(Egg.class, velocity);
                    egg.setMetadata("AbilityEgg", new FixedMetadataValue(this.plugin, morph.getAbilityDuration()));
                    this.entitiesQueue.add(egg);
                }
                p.updateInventory();
                return;
            }
            if ("cow".equals(morphType)) {
                throwItems(p, new CustomItem(Material.MILK_BUCKET, 1, 0), morph.getAbilityDuration(), "milkbucket");
                p.updateInventory();
                return;
            }
            if ("creeper".equals(morphType)) {
                p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0);
                Sounds.broadcastSound(Sound.EXPLODE, p.getLocation());
                for (Entity near : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                    near.setVelocity(near.getLocation().getDirection().multiply(-2));
                }
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (ReflectionAPI.verBiggerThan(1, 8))
                        online.spigot().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 1, 100);
                    else p.playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, null);
                }
                p.updateInventory();
                return;
            }
            if ("donkey".equals(morphType) || morphType.contains("guardian") || morphType.contains("horse") || "mule".equals(morphType) || "ocelot".equals(morphType) || "silverfish".equals(morphType) || "squid".equals(morphType) || "wolf".equals(morphType)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                p.updateInventory();
                return;
            }
            if (morphType.contains("ender")) {
                Location l = p.getTargetBlock(Collections.emptySet(), 50).getLocation();
                l = l.getWorld().getHighestBlockAt(l).getLocation();
                p.teleport(l);
                Sounds.broadcastSound(Sound.ENDERMAN_TELEPORT, p.getLocation());
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
                }
                p.updateInventory();
                return;
            }
            if ("giant".equals(morphType)) {
                for (int i = 0; i < 5; i++) {
                    LivingEntity zombie = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                    this.entitiesQueue.add(zombie);
                    zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        this.entitiesQueue.remove(zombie);
                        zombie.remove();
                    }, 20 * morph.getAbilityDuration());
                }
                p.updateInventory();
                return;
            }
            if ("iron_golem".equals(morphType)) {
                throwItems(p, new CustomItem(Material.IRON_INGOT, 1, 0), morph.getAbilityDuration(), "ironingot");
                p.updateInventory();
                return;
            }
            if ("magma_cube".equals(morphType) || "slime".equals(morphType) || "rabbit".equals(morphType)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, morph.getAbilityDuration() * 20, 1));
                p.updateInventory();
                return;
            }
            if ("mushroom_cow".equals(morphType)) {
                throwItems(p, new CustomItem(Material.RED_MUSHROOM, 1, 0), morph.getAbilityDuration(), "mushroom");
                p.updateInventory();
                return;
            }
            if ("pig".equals(morphType)) {
                throwItems(p, new CustomItem(Material.GRILLED_PORK, 1, 0), morph.getAbilityDuration(), "porkchop");
                p.updateInventory();
                return;
            }
            if ("pig_zombie".equals(morphType)) {
                for (int i = 0; i < 5; i++) {
                    Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG_ZOMBIE);
                    this.entitiesQueue.add(zombie);
                    zombie.setBaby(true);
                    zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        this.entitiesQueue.remove(zombie);
                        zombie.remove();
                    }, 20 * morph.getAbilityDuration());
                }
                p.updateInventory();
                return;
            }
            if ("sheep".equals(morphType)) {
                throwColoredWool(p, new CustomItem(Material.WOOL, 1, 0), morph.getAbilityDuration());
                p.updateInventory();
                return;
            }
            if (morphType.contains("skeleton")) {
                for (int i = 0; i < 3; i++) {
                    Vector velocity = p.getEyeLocation().getDirection();
                    velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                    velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                    velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                    Arrow arrow = p.launchProjectile(Arrow.class, velocity);
                    arrow.setMetadata("AbilityArrow", new FixedMetadataValue(this.plugin, morph.getAbilityDuration()));
                    this.entitiesQueue.add(arrow);
                }
                p.updateInventory();
                return;
            }
            if (morphType.contains("snowman")) {
                for (int i = 0; i < 3; i++) {
                    Vector velocity = p.getEyeLocation().getDirection();
                    velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
                    velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
                    velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
                    Snowball snowball = p.launchProjectile(Snowball.class, velocity);
                    snowball.setMetadata("AbilitySnowball", new FixedMetadataValue(this.plugin, morph.getAbilityDuration()));
                    this.entitiesQueue.add(snowball);
                }
                p.updateInventory();
                return;
            }
            if ("villager".equals(morphType)) {
                for (int i = 0; i < 5; i++) {
                    Ageable villager = (Ageable) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                    this.entitiesQueue.add(villager);
                    villager.setBaby();
                    villager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        this.entitiesQueue.remove(villager);
                        villager.remove();
                    }, 20 * morph.getAbilityDuration());
                }
                p.updateInventory();
                return;
            }
            if ("witch".equals(morphType)) {
                ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
                potion.setItem(new ItemStack(Material.POTION, 1, (short) 16421));
                p.updateInventory();
                return;
            }
            if ("wither".equals(morphType)) {
                Vector velocity = p.getEyeLocation().getDirection();
                velocity.multiply(2);
                WitherSkull skull = p.launchProjectile(WitherSkull.class, velocity);
                skull.setMetadata("AbilitySkull", new FixedMetadataValue(this.plugin, morph.getAbilityDuration()));
                skull.setCharged(true);
                skull.setIsIncendiary(false);
                this.entitiesQueue.add(skull);
                p.updateInventory();
                return;
            }
            if (morphType.contains("zombie")) {
                for (int i = 0; i < 5; i++) {
                    Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                    zombie.setBaby(true);
                    this.entitiesQueue.add(zombie);
                    zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, morph.getAbilityDuration() * 20, 1));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        this.entitiesQueue.remove(zombie);
                        zombie.remove();
                    }, 20 * morph.getAbilityDuration());
                }
                p.updateInventory();
            }
        }
    }

    public void cleanup() {
        this.entitiesQueue.forEach(Entity::remove);
    }

    private boolean isEntity(Entity ent) {
        if (ent.hasMetadata("AbilityItem") || ent.getCustomName() != null && ent.getCustomName().endsWith("AbilityItem"))
            return true;
        for (Entity e : this.entitiesQueue) {
            if (ent.getUniqueId().equals(e.getUniqueId())) return true;
        }
        return false;
    }

    private void throwItems(LivingEntity p, CustomItem is, int duration, String metadata) {
        for (int i = 0; i < 10; i++) {
            is.setName(Random.getInt() + "AbilityItem");
            Vector velocity = p.getEyeLocation().getDirection();
            velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
            velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
            velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), is);
            item.setVelocity(velocity);
            item.setMetadata("AbilityItem", new FixedMetadataValue(this.plugin, metadata + '-' + duration));
            this.entitiesQueue.add(item);
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                this.entitiesQueue.remove(item);
                item.remove();
            }, 20 * duration);
        }
    }

    private void throwColoredWool(LivingEntity p, CustomItem is, int duration) {
        for (int i = 0; i < 10; i++) {
            is.setDurability((short) Random.getInt(15));
            is.setName(Random.getInt() + "AbilityItem");
            Vector velocity = p.getEyeLocation().getDirection();
            velocity.setX(velocity.getX() + 0.5 * Random.getDouble(true));
            velocity.setY(velocity.getY() + 0.5 * Random.getDouble());
            velocity.setZ(velocity.getZ() + 0.5 * Random.getDouble(true));
            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), is);
            item.setVelocity(velocity);
            item.setMetadata("AbilityItem", new FixedMetadataValue(this.plugin, "wool"));
            this.entitiesQueue.add(item);
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                this.entitiesQueue.remove(item);
                item.remove();
            }, 20 * duration);
        }
    }
}
