package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.serialization.SerializedList;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.balloons.BalloonImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.banners.BannerImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.bowtrails.BowtrailImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.gadgets.GadgetImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.hats.HatImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.hearts.HeartImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.morphs.MorphImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.mounts.MountImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.music.MusicImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.particles.ParticleImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.pets.PetImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.trails.TrailImpl;
import com.j0ach1mmall3.ultimatecosmetics.internal.wardrobe.OutfitImpl;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by j0ach1mmall3 on 8:45 1/11/2015 using IntelliJ IDEA.
 */
public final class CosmeticsQueue {
    private final BalloonStorage balloon;
    private final BannerStorage banner;
    private final BowtrailStorage bowtrail;
    private final GadgetStorage gadget;
    private final HatStorage hat;
    private final HeartStorage hearts;
    private final MorphStorage morph;
    private final MountStorage mount;
    private final MusicStorage music;
    private final ParticleStorage particles;
    private final PetStorage pet;
    private final TrailStorage trail;
    private final OutfitStorage outfit;

    CosmeticsQueue(Main plugin, List<String> cosmetics) {
        CosmeticsAPI api = plugin.getApi();
        this.balloon = api.getBalloonByIdentifier(cosmetics.get(0)) != null ? api.getBalloonByIdentifier(cosmetics.get(0)) : null;
        this.banner = api.getBannerByIdentifier(cosmetics.get(1)) != null ? api.getBannerByIdentifier(cosmetics.get(1)) : null;
        this.bowtrail = api.getBowtrailByIdentifier(cosmetics.get(2)) != null ? api.getBowtrailByIdentifier(cosmetics.get(2)) : null;
        this.gadget = api.getGadgetByIdentifier(cosmetics.get(3)) != null ? api.getGadgetByIdentifier(cosmetics.get(3)) : null;
        this.hat = api.getHatByIdentifier(cosmetics.get(4)) != null ? api.getHatByIdentifier(cosmetics.get(4)) : null;
        this.hearts = api.getHeartByIdentifier(cosmetics.get(5)) != null ? api.getHeartByIdentifier(cosmetics.get(5)) : null;
        this.morph = api.getMorphByIdentifier(cosmetics.get(6)) != null ? api.getMorphByIdentifier(cosmetics.get(6)) : null;
        this.mount = api.getMountByIdentifier(cosmetics.get(7)) != null ? api.getMountByIdentifier(cosmetics.get(7)) : null;
        this.music = api.getMusicByIdentifier(cosmetics.get(8)) != null ? api.getMusicByIdentifier(cosmetics.get(8)) : null;
        this.particles = api.getParticleByIdentifier(cosmetics.get(9)) != null ? api.getParticleByIdentifier(cosmetics.get(9)) : null;
        this.pet = api.getPetByIdentifier(cosmetics.get(10)) != null ? api.getPetByIdentifier(cosmetics.get(10)) : null;
        this.trail = api.getTrailByIdentifier(cosmetics.get(11)) != null ? api.getTrailByIdentifier(cosmetics.get(11)) : null;
        this.outfit = api.getOutfitByIdentifier(cosmetics.get(12)) != null ? api.getOutfitByIdentifier(cosmetics.get(12)) : null;
    }

    CosmeticsQueue(Main plugin, String cosmetics) {
        this(plugin, new SerializedList(cosmetics).getCollection());
    }

    public CosmeticsQueue(Main plugin, Player p) {
        CosmeticsAPI api = plugin.getApi();
        this.balloon = api.hasBalloon(p) ? api.getBalloon(p).getBalloonStorage() : null;
        this.banner = api.hasBanner(p) ? api.getBanner(p).getBannerStorage() : null;
        this.bowtrail = api.hasBowtrail(p) ? api.getBowtrail(p).getBowtrailStorage() : null;
        this.gadget = api.hasGadget(p) ? api.getGadget(p) : null;
        this.hat = api.hasHat(p) ? api.getHat(p).getHatStorage() : null;
        this.hearts = api.hasHeart(p) ? api.getHeart(p).getHeartStorage() : null;
        this.morph = api.hasMorph(p) ? api.getMorph(p).getMorphStorage() : null;
        this.mount = api.hasMount(p) ? api.getMount(p).getMountStorage() : null;
        this.music = api.hasMusic(p) ? api.getMusic(p).getMusicStorage() : null;
        this.particles = api.hasParticle(p) ? api.getParticle(p).getParticleStorage() : null;
        this.pet = api.hasPet(p) ? api.getPet(p).getPetStorage() : null;
        this.trail = api.hasTrail(p) ? api.getTrail(p).getTrailStorage() : null;
        this.outfit = api.hasOutfit(p) ? api.getOutfit(p).getOutfitStorage() : null;
    }

    public void give(Player p) {
        if (this.balloon != null) new BalloonImpl(p, this.balloon).give();
        if (this.banner != null) new BannerImpl(p, this.banner).give();
        if (this.bowtrail != null) new BowtrailImpl(p, this.bowtrail).give();
        if (this.gadget != null) new GadgetImpl(p, this.gadget).give();
        if (this.hat != null) new HatImpl(p, this.hat).give();
        if (this.hearts != null) new HeartImpl(p, this.hearts).give();
        if (this.morph != null) new MorphImpl(p, this.morph).give();
        if (this.mount != null) new MountImpl(p, this.mount).give();
        if (this.music != null) new MusicImpl(p, this.music).give();
        if (this.particles != null) new ParticleImpl(p, this.particles).give();
        if (this.pet != null) new PetImpl(p, this.pet).give();
        if (this.trail != null) new TrailImpl(p, this.trail).give();
        if (this.outfit != null) new OutfitImpl(p, this.outfit).give();
    }

    public List<String> asList() {
        return Arrays.asList(this.balloon != null ? this.balloon.getIdentifier() : "", this.banner != null ? this.banner.getIdentifier() : "", this.bowtrail != null ? this.bowtrail.getIdentifier() : "", this.gadget != null ? this.gadget.getIdentifier() : "", this.hat != null ? this.hat.getIdentifier() : "", this.hearts != null ? this.hearts.getIdentifier() : "", this.morph != null ? this.morph.getIdentifier() : "", this.mount != null ? this.mount.getIdentifier() : "", this.music != null ? this.music.getIdentifier() : "", this.particles != null ? this.particles.getIdentifier() : "", this.pet != null ? this.pet.getIdentifier() : "", this.trail != null ? this.trail.getIdentifier() : "", this.outfit != null ? this.outfit.getIdentifier() : "");
    }

    public String asString() {
        return new SerializedList(asList()).getString();
    }
}
