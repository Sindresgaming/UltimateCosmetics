package com.j0ach1mmall3.ultimatecosmetics.internal.gadgets;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.CustomItem;
import com.j0ach1mmall3.jlib.methods.*;
import com.j0ach1mmall3.jlib.methods.Random;
import com.j0ach1mmall3.jlib.visual.Title;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Gadgets;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import com.j0ach1mmall3.ultimatecosmetics.internal.data.DataLoader;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by j0ach1mmall3 on 19:49 22/08/2015 using IntelliJ IDEA.
 */
public final class GadgetsListener implements Listener {
    private final Main plugin;
    private final Collection<Entity> entitiesQueue = new ArrayList<>();
    private final Map<Location, Map<Material, Byte>> blockQueue = new HashMap<>();
    private final List<String> firePlayers = new ArrayList<>();
    private final List<String> diamondShowerPlayers = new ArrayList<>();
    private final List<String> paintTrailPlayers = new ArrayList<>();
    private final Map<String, String> cooldownPlayers = new HashMap<>();
    private final Map<String, Entity> fishingPlayers = new HashMap<>();

    public GadgetsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (World world : Bukkit.getWorlds()) {
                for (Entity ent : world.getEntities()) {
                    if (ent.hasMetadata("UCEntity") && "Melon".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                        handleMelon(ent);
                    if (ent.hasMetadata("UCEntity") && "ColorBomb".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                        handleColorBomb(ent);
                    if (ent.hasMetadata("UCEntity") && "GoldFountain".equals(ent.getMetadata("UCEntity").get(0).asString()) && ent.isOnGround())
                        handleGoldFountain(ent);
                }
            }
        }, 0L, 10L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (String player : this.diamondShowerPlayers) {
                Player p = Bukkit.getPlayer(player);
                Sounds.broadcastSound(Sound.CHICKEN_EGG_POP, p.getLocation());
                Location l = p.getLocation();
                l.setY(l.getY() + 2.0);
                Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Material.DIAMOND, 1, 0, String.valueOf(Random.getInt())));
                i.setPickupDelay(Integer.MAX_VALUE);
                this.entitiesQueue.add(i);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    i.remove();
                    this.entitiesQueue.remove(i);
                }, 20L);
            }
        }, 0L, plugin.getGadgets().getIntValue("DiamondShower", "Interval"));
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getSlot() == this.plugin.getGadgets().getGadgetSlot()) {
                if (this.plugin.getApi().isGadget(e.getCurrentItem())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (this.firePlayers.contains(p.getName())) {
            Location l = p.getLocation();
            Block b = l.getBlock();
            if (b.getType() == Material.AIR) {
                Map<Material, Byte> map = new HashMap<>();
                map.put(Material.AIR, (byte) 0);
                this.blockQueue.put(l, map);
                Bukkit.getOnlinePlayers().forEach(online -> online.sendBlockChange(l, Material.FIRE, (byte) 0));
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                    b.setType(Material.AIR);
                    Bukkit.getOnlinePlayers().forEach(online -> online.sendBlockChange(l, Material.AIR, (byte) 0));
                }, 20 * this.plugin.getGadgets().getIntValue("FireTrail", "RemoveDelay"));
            }
            return;
        }
        if (this.paintTrailPlayers.contains(p.getName())) {
            Location l = p.getLocation();
            l.setY(l.getY() - 1.0);
            Block b = l.getBlock();
            setRandomClay(b, this.plugin.getGadgets().getIntValue("PaintTrail", "RestoreDelay"), this.plugin);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getHeldItemSlot() == this.plugin.getGadgets().getGadgetSlot()) {
            if (this.plugin.getApi().isGadget(e.getItemDrop().getItemStack())) {
                e.setCancelled(true);
            }
        }
    }

    @SuppressWarnings({"deprecation", "OverlyComplexMethod"})
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Gadgets config = this.plugin.getGadgets();
        DataLoader loader = this.plugin.getDataLoader();
        Lang lang = this.plugin.getLang();
        CosmeticsAPI api = this.plugin.getApi();
        String uuid = p.getUniqueId().toString();
        if (api.isGadget(p.getItemInHand())) {
            GadgetStorage gadget = api.getGadgetByItemStack(p.getItemInHand());
            if (e.getAction() != Action.PHYSICAL) {
                e.setCancelled(true);
                if (!config.isKeepGadget()) p.setItemInHand(null);
                p.updateInventory();
                if ("Enderbow".equals(gadget.getIdentifier())) {
                    if (p.isInsideVehicle()) {
                        p.sendMessage(Placeholders.parse(lang.getDismountVehicle(), p));
                        p.updateInventory();
                        return;
                    }
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    General.broadcastSound(Sound.SHOOT_ARROW, p.getLocation());
                    Arrow arrow = p.shootArrow();
                    arrow.setVelocity(arrow.getVelocity().multiply(2));
                    arrow.setMetadata("Enderbow", new FixedMetadataValue(this.plugin, p.getName()));
                }
                if ("EtherealPearl".equals(gadget.getIdentifier())) {
                    if (p.isInsideVehicle()) {
                        p.sendMessage(Placeholders.parse(lang.getDismountVehicle(), p));
                        p.updateInventory();
                        return;
                    }
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    General.broadcastSound(Sound.WITHER_SHOOT, p.getLocation());
                    EnderPearl pearl = p.launchProjectile(EnderPearl.class);
                    pearl.setShooter(null);
                    pearl.setPassenger(p);
                    pearl.setMetadata("EtherealPearl", new FixedMetadataValue(this.plugin, p.getName()));
                    pearl.setVelocity(pearl.getVelocity().multiply(config.getIntValue(gadget.getIdentifier(), "Speed")));

                }
                if ("PaintballGun".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Snowball snow = p.launchProjectile(Snowball.class);
                    snow.setMetadata("Paintball", new FixedMetadataValue(this.plugin, p.getName()));
                    snow.setVelocity(snow.getVelocity().multiply(config.getIntValue(gadget.getIdentifier(), "Speed")));

                }
                if ("FlyingPig".equals(gadget.getIdentifier())) {
                    if (p.isInsideVehicle()) {
                        p.sendMessage(Placeholders.parse(lang.getDismountVehicle(), p));
                        p.updateInventory();
                        return;
                    }
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Location l = p.getLocation();
                    l.setY(l.getY() + 3.0);
                    LivingEntity bat = (LivingEntity) p.getWorld().spawnEntity(l, EntityType.BAT);
                    bat.setMetadata("FlyingPig", new FixedMetadataValue(this.plugin, p.getName()));
                    bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                    Pig pig = (Pig) p.getWorld().spawnEntity(l, EntityType.PIG);
                    pig.setCustomName(gadget.getItem().getItemMeta().getDisplayName());
                    pig.setCustomNameVisible(true);
                    pig.setAdult();
                    pig.setAgeLock(true);
                    pig.setBreed(false);
                    pig.setSaddle(true);
                    bat.setPassenger(pig);
                    pig.setPassenger(p);

                }
                if ("BatBlaster".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Location l = p.getEyeLocation();
                    for (int a = 0; a < config.getIntValue(gadget.getIdentifier(), "Amount"); a++) {
                        Damageable bat = (Damageable) p.getWorld().spawnEntity(l, EntityType.BAT);
                        bat.setVelocity(l.getDirection().multiply(config.getIntValue(gadget.getIdentifier(), "Speed")));
                        this.entitiesQueue.add(bat);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                            bat.damage(1000.0);
                            this.entitiesQueue.remove(bat);
                        }, 20 * config.getIntValue(gadget.getIdentifier(), "RemoveDelay"));

                    }
                }
                if ("CATapult".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Location l = p.getEyeLocation();
                    General.broadcastSound(Sound.CAT_MEOW, l);
                    for (int a = 0; a < config.getIntValue(gadget.getIdentifier(), "Amount"); a++) {
                        Ocelot cat = (Ocelot) p.getWorld().spawnEntity(l, EntityType.OCELOT);
                        cat.setTamed(true);
                        Vector v = l.getDirection().multiply(config.getIntValue(gadget.getIdentifier(), "Speed"));
                        v.setX(v.getX() + Random.getDouble());
                        v.setY(v.getY() + Random.getDouble());
                        v.setZ(v.getZ() + Random.getDouble());
                        cat.setVelocity(v);
                        this.entitiesQueue.add(cat);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                            cat.getWorld().createExplosion(cat.getLocation().getX(), cat.getLocation().getY(), cat.getLocation().getZ(), config.getIntValue(gadget.getIdentifier(), "ExplosionPower"), false, false);
                            cat.remove();
                            this.entitiesQueue.remove(cat);
                        }, 20 * config.getIntValue(gadget.getIdentifier(), "ExplosionDelay"));
                    }

                }
                if ("RailGun".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Vector vec = p.getLocation().getDirection();
                    Location lastParticle = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.3, p.getEyeLocation().getZ());
                    for (int j = 0; j < config.getIntValue(gadget.getIdentifier(), "Range") << 1; j++) {
                        if (lastParticle.getBlock().getType() != Material.AIR) return;
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (ReflectionAPI.verBiggerThan(1, 8))
                                online.spigot().playEffect(lastParticle, Effect.FIREWORKS_SPARK, 0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 1, 100);
                            else p.playEffect(lastParticle, Effect.FIREWORKS_SPARK, 0);
                            General.broadcastSound(Sound.FIREWORK_LAUNCH, lastParticle);
                            lastParticle.add(vec.getX(), vec.getY(), vec.getZ());

                        }
                    }
                }
                if ("CryoTube".equals(gadget.getIdentifier())) {
                    Location l1 = p.getLocation();
                    Location l2 = new Location(l1.getWorld(), l1.getX(), l1.getY() + 1.0, l1.getZ());
                    if (l1.getBlock().getType() != Material.AIR || l2.getBlock().getType() != Material.AIR) {
                        this.plugin.informPlayerNoPermission(p, lang.getNotEnoughSpace());
                        p.updateInventory();
                        return;
                    }
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    Map<Material, Byte> map1 = new HashMap<>();
                    Block b1 = l1.getBlock();
                    map1.put(Material.AIR, (byte) 0);
                    this.blockQueue.put(l1, map1);
                    b1.setType(Material.ICE);
                    b1.setMetadata("CryoTube", new FixedMetadataValue(this.plugin, p.getName()));
                    Map<Material, Byte> map2 = new HashMap<>();
                    Block b2 = l2.getBlock();
                    map2.put(Material.AIR, (byte) 0);
                    this.blockQueue.put(l2, map2);
                    b2.setType(Material.ICE);
                    b2.setMetadata("CryoTube", new FixedMetadataValue(this.plugin, p.getName()));
                    General.broadcastSound(Sound.GLASS, l1);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (ReflectionAPI.verBiggerThan(1, 8))
                            online.spigot().playEffect(l1, Effect.STEP_SOUND, 79, 79, 0.7f, 0.7f, 0.7f, 2.0F, 10, 100);
                        else p.playEffect(l1, Effect.STEP_SOUND, Material.ICE);
                        if (ReflectionAPI.verBiggerThan(1, 8))
                            online.spigot().playEffect(l2, Effect.STEP_SOUND, 79, 79, 0.7f, 0.7f, 0.7f, 2.0F, 10, 100);
                        else p.playEffect(l2, Effect.STEP_SOUND, Material.ICE);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                            b1.setType(Material.AIR);
                            b2.setType(Material.AIR);
                            this.blockQueue.remove(l1);
                            this.blockQueue.remove(l2);
                        }, config.getIntValue(gadget.getIdentifier(), "RemoveDelay") * 20);

                    }
                }
                if ("Rocket".equals(gadget.getIdentifier())) {
                    if (p.isInsideVehicle()) {
                        p.sendMessage(Placeholders.parse(lang.getDismountVehicle(), p));
                        p.updateInventory();
                        return;
                    }
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    new GadgetsListener.RocketRunnable(config, p, gadget).runTaskTimer(this.plugin, 0L, 20L);

                }
                if ("PoopBomb".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    General.broadcastSound(Sound.CHICKEN_EGG_POP, p.getLocation());
                    for (int a = 0; a < config.getIntValue(gadget.getIdentifier(), "Amount"); a++) {
                        int x = Random.getInt(config.getIntValue(gadget.getIdentifier(), "Radius") << 1) - config.getIntValue(gadget.getIdentifier(), "Radius");
                        int z = Random.getInt(config.getIntValue(gadget.getIdentifier(), "Radius") << 1) - config.getIntValue(gadget.getIdentifier(), "Radius");
                        Location l = p.getLocation();
                        l.setX(l.getX() + x);
                        l.setY(l.getY() + 20.0);
                        l.setZ(l.getZ() + z);
                        Item item = l.getWorld().dropItemNaturally(l, new CustomItem(Material.INK_SACK, 1, (short) 3, p.getName() + ":poopbomb", new String[]{String.valueOf(Random.getInt())}));
                        item.setMetadata("PoopBomb", new FixedMetadataValue(this.plugin, p.getName()));
                        item.setPickupDelay(Integer.MAX_VALUE);
                        this.entitiesQueue.add(item);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                if (ReflectionAPI.verBiggerThan(1, 8))
                                    online.spigot().playEffect(item.getLocation(), Effect.SMOKE, 0, 0, 0.0F, 0.0F, 0.0F, 2.0F, 10, 100);
                                else p.playEffect(item.getLocation(), Effect.SMOKE, 0);
                                General.broadcastSound(Sound.FIREWORK_BLAST2, item.getLocation());
                                item.remove();
                                this.entitiesQueue.remove(item);
                            }
                        }, config.getIntValue(gadget.getIdentifier(), "RemoveDelay") * 20);

                    }
                }
                if ("GrapplingHook".equals(gadget.getIdentifier())) {
                    if (this.fishingPlayers.containsKey(p.getName()))
                        this.fishingPlayers.get(p.getName()).remove();
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    General.broadcastSound(Sound.SHOOT_ARROW, p.getLocation());
                    Fish hook = p.launchProjectile(Fish.class);
                    hook.setMetadata("GrapplingHook", new FixedMetadataValue(this.plugin, p.getName()));
                    this.fishingPlayers.put(p.getName(), hook);

                }

                if ("SelfDestruct".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0);
                    General.broadcastSound(Sound.EXPLODE, p.getLocation());
                    for (Entity near : p.getNearbyEntities(config.getIntValue(gadget.getIdentifier(), "PushbackRange"), config.getIntValue(gadget.getIdentifier(), "PushbackRange"), config.getIntValue(gadget.getIdentifier(), "PushbackRange"))) {
                        near.setVelocity(near.getLocation().getDirection().multiply(-2));
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (ReflectionAPI.verBiggerThan(1, 8))
                                online.spigot().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 1, 100);
                            else p.playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 0);
                            p.playEffect(EntityEffect.HURT);

                        }
                    }
                    if ("Slimevasion".equals(gadget.getIdentifier())) {
                        if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                            this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                            return;
                        }
                        this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                        if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                            int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                            if (i <= 0) {
                                p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                return;
                            } else {
                                loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                            }
                        }
                        for (int a = 0; a < config.getIntValue(gadget.getIdentifier(), "Amount"); a++) {
                            Slime slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                            slime.setSize(Random.getInt(1, 4));
                            slime.setMetadata("SlimeVasion", new FixedMetadataValue(this.plugin, p.getName()));
                            this.entitiesQueue.add(slime);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                                slime.remove();
                                this.entitiesQueue.remove(slime);
                            }, config.getIntValue(gadget.getIdentifier(), "RemoveDelay") * 20);

                        }
                    }
                    if ("FunGun".equals(gadget.getIdentifier())) {
                        if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                            this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                            return;
                        }
                        this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                        if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                            int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                            if (i <= 0) {
                                p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                return;
                            } else {
                                loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                            }
                        }
                        for (int a = 0; a < 5; a++) {
                            Snowball s = p.launchProjectile(Snowball.class);
                            s.setMetadata("FunGun", new FixedMetadataValue(this.plugin, p.getName()));
                            s.setShooter(p);

                        }

                        if ("MelonThrower".equals(gadget.getIdentifier())) {
                            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                                return;
                            }
                            this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                            if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                                int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                                if (i <= 0) {
                                    p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                    return;
                                } else {
                                    loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                    p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                                }
                            }
                            ItemStack ci = gadget.getItem().clone();
                            ItemMeta im = ci.getItemMeta();
                            im.setDisplayName(String.valueOf(Random.getInt()));
                            ci.setItemMeta(im);
                            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), ci);
                            item.setVelocity(p.getEyeLocation().getDirection().multiply(1.5f));
                            item.setMetadata("UCEntity", new FixedMetadataValue(this.plugin, "Melon"));
                            item.setPickupDelay(Integer.MAX_VALUE);
                            this.entitiesQueue.add(item);

                        }

                        if ("ColorBomb".equals(gadget.getIdentifier())) {
                            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                                return;
                            }
                            this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                            if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                                int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                                if (i <= 0) {
                                    p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                    return;
                                } else {
                                    loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                    p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                                }
                            }
                            ItemStack ci = gadget.getItem().clone();
                            ItemMeta im = ci.getItemMeta();
                            im.setDisplayName(String.valueOf(Random.getInt()));
                            ci.setItemMeta(im);
                            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), ci);
                            item.setVelocity(p.getEyeLocation().getDirection().multiply(1.5f));
                            item.setMetadata("UCEntity", new FixedMetadataValue(this.plugin, "ColorBomb"));
                            item.setPickupDelay(Integer.MAX_VALUE);
                            this.entitiesQueue.add(item);

                        }

                        if ("FireTrail".equals(gadget.getIdentifier())) {
                            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                                return;
                            }
                            this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                            if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                                int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                                if (i <= 0) {
                                    p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                    return;
                                } else {
                                    loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                    p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                                }
                            }
                            this.firePlayers.add(p.getName());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, config.getIntValue(gadget.getIdentifier(), "Duration") * 20, config.getIntValue(gadget.getIdentifier(), "Speed.Multiplier") - 1));
                            General.broadcastSound(Sound.BLAZE_BREATH, p.getLocation());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.firePlayers.remove(p.getName()), 20 * config.getIntValue(gadget.getIdentifier(), "Duration"));

                        }

                        if ("DiamondShower".equals(gadget.getIdentifier())) {
                            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                                return;
                            }
                            this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                            if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                                int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                                if (i <= 0) {
                                    p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                    return;
                                } else {
                                    loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                    p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                                }
                            }
                            this.diamondShowerPlayers.add(p.getName());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.diamondShowerPlayers.remove(p.getName()), 20 * config.getIntValue(gadget.getIdentifier(), "Duration"));

                        }

                        if ("GoldFountain".equals(gadget.getIdentifier())) {
                            if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                                this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                                return;
                            }
                            this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                            if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                                int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                                if (i <= 0) {
                                    p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                                    return;
                                } else {
                                    loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                                    p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                                }
                            }
                            ItemStack ci = gadget.getItem().clone();
                            ItemMeta im = ci.getItemMeta();
                            im.setDisplayName(String.valueOf(Random.getInt()));
                            ci.setItemMeta(im);
                            Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(), ci);
                            item.setVelocity(p.getEyeLocation().getDirection().multiply(1.5f));
                            item.setMetadata("UCEntity", new FixedMetadataValue(this.plugin, "GoldFountain"));
                            item.setPickupDelay(Integer.MAX_VALUE);
                            this.entitiesQueue.add(item);
                        }
                    }
                }
                if ("PaintTrail".equals(gadget.getIdentifier())) {
                    if (this.cooldownPlayers.get(p.getName()) != null && this.cooldownPlayers.get(p.getName()).split(":")[0].equals(gadget.getIdentifier())) {
                        this.plugin.informPlayerNoPermission(p, Placeholders.parse(lang.getGadgetsCooldown().replace("%timeleft%", String.valueOf(gadget.getCooldown() - (System.currentTimeMillis() - Long.valueOf(this.cooldownPlayers.get(p.getName()).split(":")[1])) / 1000))));
                        return;
                    }
                    this.cooldownPlayers.put(p.getName(), gadget.getIdentifier() + ':' + System.currentTimeMillis());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.cooldownPlayers.remove(p.getName()), 20 * gadget.getCooldown());
                    if (gadget.isUseAmmo() && !p.hasPermission("uc.unlimitedammo")) {
                        int i = loader.getAmmo(gadget.getIdentifier(), uuid);
                        if (i <= 0) {
                            p.sendMessage(Placeholders.parse(lang.getNotEnoughAmmo(), p).replace("%ammoname%", gadget.getAmmoName()));
                            return;
                        } else {
                            loader.takeAmmo(gadget.getIdentifier(), uuid, 1);
                            p.sendMessage(Placeholders.parse(lang.getLostAmmo(), p).replace("%ammoleft%", String.valueOf(i - 1)).replace("%ammoname%", gadget.getAmmoName()));
                        }
                    }
                    this.paintTrailPlayers.add(p.getName());
                    General.broadcastSound(Sound.SPLASH, p.getLocation());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.paintTrailPlayers.remove(p.getName()), 20 * config.getIntValue(gadget.getIdentifier(), "Duration"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("UCEntity") && "MelonSlice".equals(e.getItem().getMetadata("UCEntity").get(0).asString())) {
            e.setCancelled(true);
            e.getItem().remove();
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.plugin.getGadgets().getIntValue("MelonThrower", "Speed.Duration") * 20, this.plugin.getGadgets().getIntValue("MelonThrower", "Speed.Multiplier") - 1));
            Sounds.broadcastSound(Sound.BURP, e.getPlayer().getLocation());
        }
        if (isEntity(e.getItem())) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onPlayerVehicleExit(VehicleExitEvent e) {
        if (e.getVehicle().getVehicle() != null) {
            Entity ent = e.getVehicle().getVehicle();
            if (ent.hasMetadata("FlyingPig")) {
                ent.remove();
                ent.getPassenger().remove();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("SlimeVasion") || ent.hasMetadata("FlyingPig") || ent.getVehicle() != null && ent.getVehicle().hasMetadata("FlyingPig"))
            e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            double damage = e.getDamage();
            if (damage >= p.getHealth()) {
                if (this.plugin.getApi().hasGadget(p))
                    p.getInventory().setItem(this.plugin.getGadgets().getGadgetSlot(), null);
                return;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                if (p.isInsideVehicle()) {
                    e.setCancelled(true);
                    return;
                }
                Location l = p.getLocation();
                l.setY(l.getY() + 1.0);
                if (l.getBlock().getType() == Material.ICE && l.getBlock().hasMetadata("CryoTube")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("Enderbow") || e.getDamager().hasMetadata("SlimeVasion"))
            e.setCancelled(true);
        if (e.getDamager().hasMetadata("GrapplingHook")) {
            e.setCancelled(true);
            if (this.plugin.getGadgets().getBooleanValue("GrapplingHook", "PullPlayers")) {
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    Player shooter = Bukkit.getPlayer(e.getDamager().getMetadata("GrapplingHook").get(0).asString());
                    e.getDamager().remove();
                    this.fishingPlayers.remove(p.getName());
                    pullEntityToLocation(p, shooter.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.ICE && e.getBlock().hasMetadata("CryoTube")) {
            e.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Enderbow")) {
            Player p = Bukkit.getPlayer(ent.getMetadata("Enderbow").get(0).asString());
            Location l = ent.getLocation();
            l.setPitch(p.getLocation().getPitch());
            l.setYaw(p.getLocation().getYaw());
            p.teleport(l);
            Sounds.broadcastSound(Sound.ENDERMAN_TELEPORT, l);
            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0);
            ent.remove();
            return;
        }
        if (ent.hasMetadata("EtherealPearl")) {
            if (ent.getPassenger() != null) {
                if (ent.getPassenger() instanceof Player) {
                    Entity p = ent.getPassenger();
                    p.eject();
                    Location l = p.getLocation();
                    l.setY(l.getWorld().getHighestBlockYAt(l));
                    ent.remove();
                    p.teleport(l);
                }
            }
            return;
        }
        if (ent.hasMetadata("Paintball")) {
            Location l = ent.getLocation();
            Sounds.broadcastSound(Sound.SPLASH, l);
            Iterator<Block> iterator = new BlockIterator(l.getWorld(), l.toVector(), ent.getVelocity().normalize(), 0.0D, this.plugin.getGadgets().getIntValue("PaintballGun", "PaintSize"));
            while (iterator.hasNext()) {
                Block block = iterator.next();
                setRandomClay(block, this.plugin.getGadgets().getIntValue("PaintballGun", "RestoreDelay"), this.plugin);
            }
            return;
        }
        if (ent.hasMetadata("GrapplingHook")) {
            if (!ent.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
                if (ent.getNearbyEntities(1.0, 1.0, 1.0).get(0).getType() == EntityType.PLAYER) {
                    return;
                }
            }
            Player p = Bukkit.getPlayer(ent.getMetadata("GrapplingHook").get(0).asString());
            this.fishingPlayers.remove(p.getName());
            pullEntityToLocation(p, ent.getLocation());
            ent.remove();
            return;
        }
        if (ent.hasMetadata("FunGun")) {
            Location l = ent.getLocation();
            ent.remove();
            Sounds.broadcastSound(Sound.CAT_MEOW, l);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (ReflectionAPI.verBiggerThan(1, 8))
                    p.spigot().playEffect(l, Effect.HEART, 0, 0, 0.0f, 0.2f, 0.0f, 2.0F, 1, 100);
                else p.playEffect(l, Effect.HEART, 0);
                if (ReflectionAPI.verBiggerThan(1, 8))
                    p.spigot().playEffect(l, Effect.LAVA_POP, 0, 0, 0.7f, 0.7f, 0.7f, 2.0F, 10, 100);
                else p.playEffect(l, Effect.LAVA_POP, 0);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        p.getNearbyEntities(20.0, 20.0, 20.0).stream().filter(this::isEntity).forEach(Entity::remove);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        p.getNearbyEntities(20.0, 20.0, 20.0).stream().filter(this::isEntity).forEach(Entity::remove);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        p.getNearbyEntities(20.0, 20.0, 20.0).stream().filter(this::isEntity).forEach(Entity::remove);
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent e) {
        if (isEntity(e.getItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (isEntity(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e) {
        if (isEntity(e.getEntity())) e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    private void setRandomClay(Block block, int delay, Plugin plugin) {
        if (block.getType() == Material.AIR || block.getTypeId() == 166 || this.blockQueue.containsKey(block.getLocation()))
            return;
        Material mat = block.getType();
        byte data = block.getData();
        Map<Material, Byte> map = new HashMap<>();
        map.put(mat, data);
        this.blockQueue.put(block.getLocation(), map);
        Bukkit.getOnlinePlayers().forEach(online -> online.sendBlockChange(block.getLocation(), Material.STAINED_CLAY, (byte) Random.getInt(15)));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (ReflectionAPI.verBiggerThan(1, 8))
                p.spigot().playEffect(block.getLocation(), Effect.MAGIC_CRIT, 0, 0, 0.7f, 0.7f, 0.7f, 2.0F, 10, 100);
            else p.playEffect(block.getLocation(), Effect.MAGIC_CRIT, 0);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(online -> online.sendBlockChange(block.getLocation(), mat, data));
            block.setType(mat);
            block.setData(data);
            this.blockQueue.remove(block.getLocation());
        }, delay * 20);
    }

    private void pullEntityToLocation(Entity p, Location l) {
        Location el = p.getLocation();
        Sounds.broadcastSound(Sound.PISTON_EXTEND, el);
        el.setY(el.getY() + 0.5);
        p.teleport(el);
        double g = -0.08;
        double t = l.distance(el);
        double vx = 1.07 * t * (l.getX() - el.getX()) / t;
        double vy = 1.03 * t * (l.getY() - el.getY()) / t - 0.5 * g * t;
        double vz = 1.07 * t * (l.getZ() - el.getZ()) / t;
        Vector v = p.getVelocity();
        v.setX(vx);
        v.setY(vy);
        v.setZ(vz);
        p.setVelocity(v);
        p.setFallDistance(-100.0F);
    }

    private void handleMelon(Entity ent) {
        Location l = ent.getLocation();
        this.entitiesQueue.remove(ent);
        ent.remove();
        Sounds.broadcastSound(Sound.EXPLODE, l);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (ReflectionAPI.verBiggerThan(1, 8))
                p.spigot().playEffect(l, Effect.STEP_SOUND, 103, 103, 0.7f, 0.7f, 0.7f, 2.0F, 10, 100);
            else p.playEffect(l, Effect.STEP_SOUND, Material.MELON_BLOCK);
        }
        for (int a = 0; a < this.plugin.getGadgets().getIntValue("MelonThrower", "Amount"); a++) {
            Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Material.MELON, 1, 0, Random.getInt() + "UCEntity"));
            this.entitiesQueue.add(i);
            i.setMetadata("UCEntity", new FixedMetadataValue(this.plugin, "MelonSlice"));
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                i.remove();
                this.entitiesQueue.remove(i);
            }, this.plugin.getGadgets().getIntValue("MelonThrower", "RemoveDelay") * 20);
        }
    }

    private void handleColorBomb(Entity ent) {
        Location l = ent.getLocation();
        this.entitiesQueue.remove(ent);
        ent.remove();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                this.count++;
                if (this.count > GadgetsListener.this.plugin.getGadgets().getIntValue("ColorBomb", "RemoveDelay") * 20) {
                    this.cancel();
                } else {
                    Sounds.broadcastSound(Sound.CHICKEN_EGG_POP, l);
                    String item = GadgetsListener.this.plugin.getGadgets().getStringList("ColorBomb", "Items").get(Random.getInt(GadgetsListener.this.plugin.getGadgets().getStringList("ColorBomb", "Items").size() - 1));
                    Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Parsing.parseMaterial(item), 1, Parsing.parseData(item), Random.getInt() + "UCEntity"));
                    i.setMetadata("UCEntity", new FixedMetadataValue(GadgetsListener.this.plugin, "ColorBomb"));
                    GadgetsListener.this.entitiesQueue.add(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GadgetsListener.this.plugin, () -> {
                        i.remove();
                        GadgetsListener.this.entitiesQueue.remove(i);
                    }, 5L);
                }
            }
        }.runTaskTimer(this.plugin, 0L, 1L);
    }

    private void handleGoldFountain(Entity ent) {
        Location l = ent.getLocation();
        this.entitiesQueue.remove(ent);
        ent.remove();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                this.count++;
                if (this.count > GadgetsListener.this.plugin.getGadgets().getIntValue("GoldFountain", "RemoveDelay") * 20) {
                    this.cancel();
                } else {
                    Sounds.broadcastSound(Sound.CHICKEN_EGG_POP, l);
                    Item i = l.getWorld().dropItemNaturally(l, new CustomItem(Material.GOLD_INGOT, 1, 0, Random.getInt() + "UCEntity"));
                    i.setMetadata("UCEntity", new FixedMetadataValue(GadgetsListener.this.plugin, "GoldFountain"));
                    GadgetsListener.this.entitiesQueue.add(i);
                    i.setVelocity(i.getVelocity().setY(0.75));
                    i.setPickupDelay(Integer.MAX_VALUE);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GadgetsListener.this.plugin, () -> {
                        i.remove();
                        GadgetsListener.this.entitiesQueue.remove(i);
                    }, 5L);
                }
            }
        }.runTaskTimer(this.plugin, 0L, 1L);
    }

    @SuppressWarnings("deprecation")
    public void cleanup() {
        this.entitiesQueue.forEach(Entity::remove);
        for (Map.Entry<Location, Map<Material, Byte>> locationHashMapEntry : this.blockQueue.entrySet()) {
            Block block = locationHashMapEntry.getKey().getWorld().getBlockAt(locationHashMapEntry.getKey());
            for (Material m : locationHashMapEntry.getValue().keySet()) {
                Bukkit.getOnlinePlayers().forEach(online -> online.sendBlockChange(locationHashMapEntry.getKey(), m, locationHashMapEntry.getValue().get(m)));
                block.setType(m);
                block.setData(locationHashMapEntry.getValue().get(m));
            }
        }
    }

    private boolean isEntity(Entity ent) {
        if (ent.hasMetadata("UCEntity") || ent instanceof LivingEntity && ent.getCustomName() != null && ent.getCustomName().endsWith("UCEntity"))
            return true;
        for (Entity e : this.entitiesQueue) {
            if (ent.getUniqueId().equals(e.getUniqueId())) return true;
        }
        return false;
    }

    private static final class RocketRunnable extends BukkitRunnable {
        private final Gadgets config;
        private final Player p;
        private final GadgetStorage gadget;
        int counter;

        private RocketRunnable(Gadgets config, Player p, GadgetStorage gadget) {
            this.config = config;
            this.p = p;
            this.gadget = gadget;
            this.counter = config.getIntValue("Rocket", "Countdown");
        }

        @Override
        public void run() {
            if (this.counter > 0) {
                if (ReflectionAPI.verBiggerThan(1, 8))
                    new Title(this.p, ChatColor.RED + String.valueOf(this.counter), 5, 10, 5).send();
                else this.p.sendMessage(ChatColor.RED + String.valueOf(this.counter));
                Sounds.playSound(this.p, Sound.NOTE_STICKS);
                this.counter--;
            } else {
                if (ReflectionAPI.verBiggerThan(1, 8))
                    new Title(this.p, ChatColor.RED + "Lift-Off", 0, 10, 0).send();
                else this.p.sendMessage(ChatColor.RED + "Lift-Off");
                Firework fw = this.p.getWorld().spawn(this.p.getLocation(), Firework.class);
                FireworkMeta fm = fw.getFireworkMeta();
                FireworkEffect.Builder effect = FireworkEffect.builder();
                effect.with(FireworkEffect.Type.BALL_LARGE);
                effect.withColor(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                effect.withFade(Color.fromRGB(Random.getInt(255), Random.getInt(255), Random.getInt(255)));
                effect.withTrail();
                fm.addEffect(effect.build());
                fm.setPower(4);
                fw.setFireworkMeta(fm);
                fw.setPassenger(this.p);
                fw.setVelocity(new Vector(0, this.config.getIntValue(this.gadget.getIdentifier(), "Speed"), 0));
                this.p.setFallDistance(-100.0F);
                Sounds.broadcastSound(Sound.EXPLODE, this.p.getLocation());
                this.cancel();
            }
        }
    }
}
