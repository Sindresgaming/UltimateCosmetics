package com.j0ach1mmall3.ultimatecosmetics.internal.gui;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.GUI;
import com.j0ach1mmall3.jlib.inventory.GuiItem;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.events.PlayerOpenGuiEvent;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Config;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by j0ach1mmall3 on 0:38 4/10/2015 using IntelliJ IDEA.
 */
public final class CosmeticsGuiHandler extends GuiHandler {
    public CosmeticsGuiHandler(Main plugin) {
        super(plugin);
    }

    public static void open(Player p) {
        Player p1 = p;
        PAGEMAP.remove(p1.getName());
        Config config = plugin.getBabies();
        GUI gui = new GUI(Placeholders.parse(config.getCosmeticsGuiName(), p1), config.getCosmeticsGuiSize());
        for (GuiItem item : config.getCosmeticsGuiItems()) {
            gui.setItem(item.getPosition(), item.getItem());
        }
        PlayerOpenGuiEvent event = new PlayerOpenGuiEvent(p1, gui);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        gui = event.getGui();
        p1 = event.getPlayer();
        gui.open(p1);
    }

    @Override
    protected void handleClick(GUI gui, Player p, ItemStack item) {
        Config config = plugin.getBabies();
        Pagination pagination = plugin.getPagination();
        if (pagination.getHomeItem().getItem().isSimilar(item)) {
            if (config.getGuiClickSound() != null) Sounds.playSound(p, config.getGuiClickSound());
            open(p);
            return;
        }
        if (gui.getName().equals(Placeholders.parse(config.getCosmeticsGuiName(), p))) {
            String command = config.getCommandByItemStack(item);
            PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, command);
            plugin.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) p.performCommand(command.substring(1));
            if (config.getGuiClickSound() != null) Sounds.playSound(p, config.getGuiClickSound());
        }
    }
}
