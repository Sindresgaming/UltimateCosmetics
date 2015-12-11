package com.j0ach1mmall3.ultimatecosmetics.api;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.*;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.*;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 20:45 17/08/2015 using IntelliJ IDEA.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public final class CosmeticsAPI {
    private final Main plugin;
    private final List<Balloon> balloons = new ArrayList<>();
    private final List<Banner> banners = new ArrayList<>();
    private final List<Bowtrail> bowtrails = new ArrayList<>();
    private final List<Morph> morphs = new ArrayList<>();
    private final List<Hat> hats = new ArrayList<>();
    private final List<Heart> hearts = new ArrayList<>();
    private final List<Mount> mounts = new ArrayList<>();
    private final List<Music> music = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final List<Pet> pets = new ArrayList<>();
    private final List<Trail> trails = new ArrayList<>();
    private final List<Outfit> outfits = new ArrayList<>();

    public CosmeticsAPI(Main plugin) {
        this.plugin = plugin;
    }

    public Balloon getBalloon(AnimalTamer p) {
        for (Balloon balloon : this.balloons) {
            if (balloon.getPlayer().getName().equals(p.getName())) return balloon;
        }
        return null;
    }

    public boolean hasBalloon(AnimalTamer p) {
        for (Balloon balloon : this.balloons) {
            if (balloon.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addBalloon(Balloon balloon) {
        this.balloons.add(balloon);
    }

    public void removeBalloon(Balloon balloon) {
        this.balloons.remove(balloon);
    }

    public Collection<Balloon> getBalloons() {
        return Collections.unmodifiableList(this.balloons);
    }

    public Banner getBanner(AnimalTamer p) {
        for (Banner banner : this.banners) {
            if (banner.getPlayer().getName().equals(p.getName())) return banner;
        }
        return null;
    }

    public boolean hasBanner(AnimalTamer p) {
        for (Banner banner : this.banners) {
            if (banner.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addBanner(Banner banner) {
        this.banners.add(banner);
    }

    public void removeBanner(Banner banner) {
        this.banners.remove(banner);
    }

    public List<Banner> getBanners() {
        return Collections.unmodifiableList(this.banners);
    }

    public Bowtrail getBowtrail(AnimalTamer p) {
        for (Bowtrail bowtrail : this.bowtrails) {
            if (bowtrail.getPlayer().getName().equals(p.getName())) return bowtrail;
        }
        return null;
    }

    public boolean hasBowtrail(AnimalTamer p) {
        for (Bowtrail bowtrail : this.bowtrails) {
            if (bowtrail.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addBowtrail(Bowtrail bowtrail) {
        this.bowtrails.add(bowtrail);
    }

    public void removeBowtrail(Bowtrail bowtrail) {
        this.bowtrails.remove(bowtrail);
    }

    public List<Bowtrail> getBowtrails() {
        return Collections.unmodifiableList(this.bowtrails);
    }

    public Morph getMorph(AnimalTamer p) {
        for (Morph morph : this.morphs) {
            if (morph.getPlayer().getName().equals(p.getName())) return morph;
        }
        return null;
    }

    public boolean hasMorph(AnimalTamer p) {
        for (Morph morph : this.morphs) {
            if (morph.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addMorph(Morph morph) {
        this.morphs.add(morph);
    }

    public void removeMorph(Morph morph) {
        this.morphs.remove(morph);
    }

    public List<Morph> getMorphs() {
        return Collections.unmodifiableList(this.morphs);
    }

    public boolean hasGadget(InventoryHolder p) {
        return isGadget(p.getInventory().getItem(this.plugin.getGadgets().getGadgetSlot()));
    }

    public boolean isGadget(ItemStack is) {
        for (GadgetStorage storage : this.plugin.getGadgets().getGadgets()) {
            if (isSimilarToGadget(storage.getItem(), is)) return true;
        }
        return false;
    }

    public GadgetStorage getGadget(InventoryHolder p) {
        int gadgetSlot = this.plugin.getGadgets().getGadgetSlot();
        return getGadgetByItemStack(p.getInventory().getItem(gadgetSlot));
    }

    private boolean isSimilarToGadget(ItemStack gadget, ItemStack stack) {
        if (stack == null) return false;
        if (!stack.hasItemMeta()) return false;
        if (!stack.getItemMeta().hasDisplayName() || !stack.getItemMeta().hasLore()) return false;
        String name = stack.getItemMeta().getDisplayName();
        return name.equals(gadget.getItemMeta().getDisplayName());
    }

    public Hat getHat(AnimalTamer p) {
        for (Hat hat : this.hats) {
            if (hat.getPlayer().getName().equals(p.getName())) return hat;
        }
        return null;
    }

    public boolean hasHat(AnimalTamer p) {
        for (Hat hat : this.hats) {
            if (hat.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addHat(Hat hat) {
        this.hats.add(hat);
    }

    public void removeHat(Hat hat) {
        this.hats.remove(hat);
    }

    public List<Hat> getHats() {
        return Collections.unmodifiableList(this.hats);
    }

    public Heart getHeart(AnimalTamer p) {
        for (Heart heart : this.hearts) {
            if (heart.getPlayer().getName().equals(p.getName())) return heart;
        }
        return null;
    }

    public boolean hasHeart(AnimalTamer p) {
        for (Heart heart : this.hearts) {
            if (heart.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addHeart(Heart heart) {
        this.hearts.add(heart);
    }

    public void removeHeart(Heart heart) {
        this.hearts.remove(heart);
    }

    public Iterable<Heart> getHearts() {
        return Collections.unmodifiableList(this.hearts);
    }

    public Mount getMount(AnimalTamer p) {
        for (Mount mount : this.mounts) {
            if (mount.getPlayer().getName().equals(p.getName())) return mount;
        }
        return null;
    }

    public boolean hasMount(AnimalTamer p) {
        for (Mount mount : this.mounts) {
            if (mount.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addMount(Mount mount) {
        this.mounts.add(mount);
    }

    public void removeMount(Mount mount) {
        this.mounts.remove(mount);
    }

    public Iterable<Mount> getMounts() {
        return Collections.unmodifiableList(this.mounts);
    }

    public Music getMusic(AnimalTamer p) {
        for (Music musicc : this.music) {
            if (musicc.getPlayer().getName().equals(p.getName())) return musicc;
        }
        return null;
    }

    public boolean hasMusic(AnimalTamer p) {
        for (Music musicc : this.music) {
            if (musicc.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addMusic(Music musicc) {
        this.music.add(musicc);
    }

    public void removeMusic(Music musicc) {
        this.music.remove(musicc);
    }

    public List<Music> getMusic() {
        return Collections.unmodifiableList(this.music);
    }

    public Particle getParticle(AnimalTamer p) {
        for (Particle particle : this.particles) {
            if (particle.getPlayer().getName().equals(p.getName())) return particle;
        }
        return null;
    }

    public boolean hasParticle(AnimalTamer p) {
        for (Particle particle : this.particles) {
            if (particle.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }

    public void removeParticle(Particle particle) {
        this.particles.remove(particle);
    }

    public Iterable<Particle> getParticles() {
        return Collections.unmodifiableList(this.particles);
    }

    public Pet getPet(AnimalTamer p) {
        for (Pet pet : this.pets) {
            if (pet.getPlayer().getName().equals(p.getName())) return pet;
        }
        return null;
    }

    public boolean hasPet(AnimalTamer p) {
        for (Pet pet : this.pets) {
            if (pet.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addPet(Pet pet) {
        this.pets.add(pet);
    }

    public void removePet(Pet pet) {
        this.pets.remove(pet);
    }

    public Iterable<Pet> getPets() {
        return Collections.unmodifiableList(this.pets);
    }

    public Trail getTrail(AnimalTamer p) {
        for (Trail trail : this.trails) {
            if (trail.getPlayer().getName().equals(p.getName())) return trail;
        }
        return null;
    }

    public boolean hasTrail(AnimalTamer p) {
        for (Trail trail : this.trails) {
            if (trail.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addTrail(Trail trail) {
        this.trails.add(trail);
    }

    public void removeTrail(Trail trail) {
        this.trails.remove(trail);
    }

    public Iterable<Trail> getTrails() {
        return Collections.unmodifiableList(this.trails);
    }

    public Outfit getOutfit(AnimalTamer p) {
        for (Outfit outfit : this.outfits) {
            if (outfit.getPlayer().getName().equals(p.getName())) return outfit;
        }
        return null;
    }

    public boolean hasOutfit(AnimalTamer p) {
        for (Outfit outfit : this.outfits) {
            if (outfit.getPlayer().getName().equals(p.getName())) return true;
        }
        return false;
    }

    public void addOutfit(Outfit outfit) {
        this.outfits.add(outfit);
    }

    public void removeOutfit(Outfit outfit) {
        this.outfits.remove(outfit);
    }

    public List<Outfit> getOutfits() {
        return Collections.unmodifiableList(this.outfits);
    }

    public BalloonStorage getBalloonByItemStack(ItemStack itemStack) {
        if(!this.plugin.getBalloons().isEnabled()) return null;
        for (BalloonStorage cosmetic : this.plugin.getBalloons().getBalloons()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public BannerStorage getBannerByItemStack(ItemStack itemStack) {
        if(!this.plugin.getBanners().isEnabled()) return null;
        for (BannerStorage cosmetic : this.plugin.getBanners().getBanners()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public BowtrailStorage getBowtrailByItemStack(ItemStack itemStack) {
        if(!this.plugin.getBowtrails().isEnabled()) return null;
        for (BowtrailStorage cosmetic : this.plugin.getBowtrails().getBowtrails()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public FireworkStorage getFireworkByItemStack(ItemStack itemStack) {
        if(!this.plugin.getFireworks().isEnabled()) return null;
        for (FireworkStorage cosmetic : this.plugin.getFireworks().getFireworks()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public GadgetStorage getGadgetByItemStack(ItemStack itemStack) {
        if(!this.plugin.getGadgets().isEnabled()) return null;
        for (GadgetStorage cosmetic : this.plugin.getGadgets().getGadgets()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public MorphStorage getMorphByItemStack(ItemStack is) {
        if(!this.plugin.getMorphs().isEnabled()) return null;
        for (MorphStorage morph : this.plugin.getMorphs().getMorphs()) {
            if (morph.getItem().getItemMeta() instanceof SkullMeta && is.getItemMeta() instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) morph.getItem().getItemMeta();
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                if (skullMeta.getOwner().equals(meta.getOwner()) && skullMeta.getDisplayName().equals(meta.getDisplayName()) && skullMeta.getLore().equals(meta.getLore()))
                    return morph;
            } else {
                if (morph.getItem().isSimilar(is)) return morph;
            }
        }
        return null;
    }

    public MountStorage getMountByItemStack(ItemStack itemStack) {
        if(!this.plugin.getMounts().isEnabled()) return null;
        for (MountStorage cosmetic : this.plugin.getMounts().getMounts()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public MusicStorage getMusicByItemStack(ItemStack itemStack) {
        if(!this.plugin.getMusic().isEnabled()) return null;
        for (MusicStorage cosmetic : this.plugin.getMusic().getMusics()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public HeartStorage getHeartByItemStack(ItemStack itemStack) {
        if(!this.plugin.getHearts().isEnabled()) return null;
        for (HeartStorage cosmetic : this.plugin.getHearts().getHearts()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public HatStorage getHatByItemStack(ItemStack is) {
        if(!this.plugin.getHats().isEnabled()) return null;
        for (HatStorage hat : this.plugin.getHats().getHats()) {
            if (hat.getItem().getItemMeta() instanceof SkullMeta && is.getItemMeta() instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) hat.getItem().getItemMeta();
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                if (skullMeta.getOwner().equals(meta.getOwner()) && skullMeta.getDisplayName().equals(meta.getDisplayName()) && skullMeta.getLore().equals(meta.getLore()))
                    return hat;
            } else {
                if (hat.getItem().isSimilar(is)) return hat;
            }
        }
        return null;
    }

    public PetStorage getPetByItemStack(ItemStack itemStack) {
        if(!this.plugin.getPets().isEnabled()) return null;
        for (PetStorage cosmetic : this.plugin.getPets().getPets()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public ParticleStorage getParticleByItemStack(ItemStack itemStack) {
        if(!this.plugin.getParticles().isEnabled()) return null;
        for (ParticleStorage cosmetic : this.plugin.getParticles().getParticles()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public TrailStorage getTrailByItemStack(ItemStack itemStack) {
        if(!this.plugin.getTrails().isEnabled()) return null;
        for (TrailStorage cosmetic : this.plugin.getTrails().getTrails()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public OutfitStorage getOutfitByItemStack(ItemStack itemStack) {
        if(!this.plugin.getWardrobe().isEnabled()) return null;
        for (OutfitStorage cosmetic : this.plugin.getWardrobe().getWardrobe()) {
            if (cosmetic.getItem().isSimilar(itemStack)) return cosmetic;
        }
        return null;
    }

    public BalloonStorage getBalloonByIdentifier(String identifier) {
        if(!this.plugin.getBalloons().isEnabled()) return null;
        for (BalloonStorage cosmetic : this.plugin.getBalloons().getBalloons()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public BannerStorage getBannerByIdentifier(String identifier) {
        if(!this.plugin.getBanners().isEnabled()) return null;
        for (BannerStorage cosmetic : this.plugin.getBanners().getBanners()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public BowtrailStorage getBowtrailByIdentifier(String identifier) {
        if(!this.plugin.getBowtrails().isEnabled()) return null;
        for (BowtrailStorage cosmetic : this.plugin.getBowtrails().getBowtrails()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public FireworkStorage getFireworkByIdentifier(String identifier) {
        if(!this.plugin.getFireworks().isEnabled()) return null;
        for (FireworkStorage cosmetic : this.plugin.getFireworks().getFireworks()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public GadgetStorage getGadgetByIdentifier(String identifier) {
        if(!this.plugin.getGadgets().isEnabled()) return null;
        for (GadgetStorage cosmetic : this.plugin.getGadgets().getGadgets()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public HatStorage getHatByIdentifier(String identifier) {
        if(!this.plugin.getHats().isEnabled()) return null;
        for (HatStorage cosmetic : this.plugin.getHats().getHats()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public HeartStorage getHeartByIdentifier(String identifier) {
        if(!this.plugin.getHearts().isEnabled()) return null;
        for (HeartStorage cosmetic : this.plugin.getHearts().getHearts()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public MorphStorage getMorphByIdentifier(String identifier) {
        if(!this.plugin.getMorphs().isEnabled()) return null;
        for (MorphStorage cosmetic : this.plugin.getMorphs().getMorphs()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public MountStorage getMountByIdentifier(String identifier) {
        if(!this.plugin.getMounts().isEnabled()) return null;
        for (MountStorage cosmetic : this.plugin.getMounts().getMounts()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public MusicStorage getMusicByIdentifier(String identifier) {
        if(!this.plugin.getMusic().isEnabled()) return null;
        for (MusicStorage cosmetic : this.plugin.getMusic().getMusics()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public ParticleStorage getParticleByIdentifier(String identifier) {
        if(!this.plugin.getParticles().isEnabled()) return null;
        for (ParticleStorage cosmetic : this.plugin.getParticles().getParticles()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public PetStorage getPetByIdentifier(String identifier) {
        if(!this.plugin.getPets().isEnabled()) return null;
        for (PetStorage cosmetic : this.plugin.getPets().getPets()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public TrailStorage getTrailByIdentifier(String identifier) {
        if(!this.plugin.getTrails().isEnabled()) return null;
        for (TrailStorage cosmetic : this.plugin.getTrails().getTrails()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }

    public OutfitStorage getOutfitByIdentifier(String identifier) {
        if(!this.plugin.getWardrobe().isEnabled()) return null;
        for (OutfitStorage cosmetic : this.plugin.getWardrobe().getWardrobe()) {
            if (cosmetic.getIdentifier().equals(identifier)) return cosmetic;
        }
        return null;
    }
}
