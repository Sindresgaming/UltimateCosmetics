package com.j0ach1mmall3.ultimatecosmetics.internal.music;

import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Music;
import com.xxmicloxx.noteblockapi.SongEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by j0ach1mmall3 on 13:52 23/08/2015 using IntelliJ IDEA.
 */
public final class MusicListener implements Listener {
    private final Main plugin;

    public MusicListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSongEnd(SongEndEvent e) {
        CosmeticsAPI api = this.plugin.getApi();
        e.getSongPlayer().getPlayerList().stream().filter(p -> api.hasMusic(Bukkit.getPlayer(p))).forEach(p -> {
            Music musicc = api.getMusic(Bukkit.getPlayer(p));
            if (this.plugin.getMusic().isLoop()) {
                musicc.give();
            } else {
                musicc.remove();
            }
        });
    }
}
