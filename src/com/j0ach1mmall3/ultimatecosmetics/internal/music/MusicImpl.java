package com.j0ach1mmall3.ultimatecosmetics.internal.music;


import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Music;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticGiveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.events.CosmeticRemoveEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.MusicStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Lang;
import com.xxmicloxx.noteblockapi.NBSDecoder;
import com.xxmicloxx.noteblockapi.RadioSongPlayer;
import com.xxmicloxx.noteblockapi.Song;
import com.xxmicloxx.noteblockapi.SongPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by j0ach1mmall3 on 10:49 22/08/2015 using IntelliJ IDEA.
 */
public final class MusicImpl implements Music {
    private Player player;
    private SongPlayer songPlayer = null;
    private MusicStorage musicStorage;

    public MusicImpl(Player player, MusicStorage musicStorage) {
        this.player = player;
        this.musicStorage = musicStorage;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public MusicStorage getMusicStorage() {
        return this.musicStorage;
    }

    @Override
    public void give() {
        CosmeticsAPI api = this.musicStorage.getPlugin().getApi();
        if (api.hasMusic(this.player)) {
            api.getMusic(this.player).remove();
            give();
        } else {
            CosmeticGiveEvent event = new CosmeticGiveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Music cosmetic = (Music) event.getCosmetic();
            this.player = cosmetic.getPlayer();
            this.musicStorage = cosmetic.getMusicStorage();
            com.j0ach1mmall3.ultimatecosmetics.internal.config.Music config = this.musicStorage.getPlugin().getMusic();
            Lang lang = this.musicStorage.getPlugin().getLang();
            File song = new File(config.getSongsPath(), this.musicStorage.getSongName() + ".nbs");
            if (!song.exists()) {
                this.musicStorage.getPlugin().informPlayerNoPermission(this.player, Placeholders.parse(lang.getSongNotFound(), this.player));
                return;
            }
            Song s = NBSDecoder.parse(song);
            SongPlayer sp = new RadioSongPlayer(s);
            sp.setAutoDestroy(true);
            sp.addPlayer(this.player);
            sp.setPlaying(true);
            this.songPlayer = sp;
            api.addMusic(this);
        }
    }

    @Override
    public void remove() {
        CosmeticsAPI api = this.musicStorage.getPlugin().getApi();
        if (api.hasMusic(this.player)) {
            CosmeticRemoveEvent event = new CosmeticRemoveEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            Cosmetic cosmetic = event.getCosmetic();
            this.player = cosmetic.getPlayer();
            api.removeMusic(this);
            this.songPlayer.setPlaying(false);
        }
    }
}
