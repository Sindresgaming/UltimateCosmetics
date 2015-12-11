package com.j0ach1mmall3.ultimatecosmetics.internal.balloons;

import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Balloon;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.BalloonStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by j0ach1mmall3 on 9:55 22/08/2015 using IntelliJ IDEA.
 */
public final class BalloonImpl implements Balloon {
    private Player player;
    private Bat bat = null;
    private FallingBlock block = null;
    private BalloonStorage balloonStorage;

    public BalloonImpl(Player player, BalloonStorage balloonStorage) {
        this.player = player;
        this.balloonStorage = balloonStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public BalloonStorage getBalloonStorage() {
        return this.balloonStorage;
    }

    @Override
    public Bat getBat() {
        return this.bat;
    }

    @Override
    public FallingBlock getBlock() {
        return this.block;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void give() {
        CosmeticsAPI api = this.balloonStorage.getPlugin().getApi();
        if (api.hasBalloon(this.player)) {
            api.getBalloon(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Balloon cosmetic = (Balloon) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.bat = cosmetic.getBat();
            this.block = cosmetic.getBlock();
            this.balloonStorage = cosmetic.getBalloonStorage();
            Location l = this.player.getLocation();
            l.setY(l.getY() + 3.0);
            this.bat = (Bat) this.player.getWorld().spawnEntity(l, EntityType.BAT);
            this.bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 100));
            this.block = this.player.getWorld().spawnFallingBlock(l, this.balloonStorage.getItem().getType(), (byte) this.balloonStorage.getItem().getDurability());
            this.block.setDropItem(false);
            this.bat.setLeashHolder(this.player);
            this.bat.setPassenger(this.block);
            this.bat.setRemoveWhenFarAway(false);
            this.bat.setMetadata("Balloon", new FixedMetadataValue(this.balloonStorage.getPlugin(), this.player.getName()));
            api.addBalloon(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.balloonStorage.getPlugin().getApi();
        if (api.hasBalloon(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Balloon cosmetic = (Balloon) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.bat = cosmetic.getBat();
            this.block = cosmetic.getBlock();
            api.removeBalloon(this);
            this.bat.setLeashHolder(null);
            this.player.setLeashHolder(null);
            this.block.remove();
            this.bat.remove();
        }
    }
}
