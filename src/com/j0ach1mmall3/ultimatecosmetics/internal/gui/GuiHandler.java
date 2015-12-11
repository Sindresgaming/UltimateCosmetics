package com.j0ach1mmall3.ultimatecosmetics.internal.gui;

import com.j0ach1mmall3.jlib.inventory.CustomEnchantment;
import com.j0ach1mmall3.jlib.inventory.GUI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.events.PlayerClickInGuiEvent;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by j0ach1mmall3 on 14:23 3/10/2015 using IntelliJ IDEA.
 */
abstract class GuiHandler implements Listener {
    private static final Collection<GUI> GUIS = new ArrayList<>();
    private static boolean unregistered = true;
    static Enchantment glow;
    static Main plugin;
    static final HashMap<String, Integer> PAGEMAP = new HashMap<>();

    GuiHandler(Main plugin) {
        this.plugin = plugin;
        CustomEnchantment ce = new CustomEnchantment("COSMETICGLOW", null, null, 1, 10);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (unregistered) {
            ce.register();
            unregistered = false;
        }
        glow = ce.getEnchantment();
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent e) {
        GUIS.clear();
        GUIS.add(new GUI(plugin.getBalloons().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getBanners().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getBowtrails().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getFireworks().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getGadgets().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getHats().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getHearts().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getMorphs().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getMounts().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getMusic().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getParticles().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getPets().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getTrails().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getWardrobe().getGuiName(), 9));
        GUIS.add(new GUI(plugin.getBabies().getCosmeticsGuiName(), 9));
        for (GUI gui : GUIS) {
            if (gui.hasClicked(e)) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (item.getItemMeta().getDisplayName().isEmpty()) return;
                Player p = (Player) e.getWhoClicked();
                Pagination pagination = plugin.getPagination();
                if (pagination.getHomeItem().getItem().isSimilar(item)) return;
                if (item.containsEnchantment(glow)) return;
                PlayerClickInGuiEvent event = new PlayerClickInGuiEvent(p, gui, item);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                p = event.getPlayer();
                gui = event.getGui();
                item = event.getClickedItem();
                handleClick(gui, p, item);
            }
        }
    }

    @EventHandler
    public final void onQuit(PlayerQuitEvent e) {
        PAGEMAP.remove(e.getPlayer().getName());
    }

    @EventHandler
    public final void onKick(PlayerKickEvent e) {
        PAGEMAP.remove(e.getPlayer().getName());
    }

    protected abstract void handleClick(GUI gui, Player p, ItemStack item);

    static GUI buildGui(String name, int size) {
        GUI gui = new GUI(name, size);
        Pagination pagination = plugin.getPagination();
        gui.setItem(pagination.getHomeItem().getPosition(), pagination.getHomeItem().getItem());
        gui.setItem(pagination.getPreviousItem().getPosition(), pagination.getPreviousItem().getItem());
        gui.setItem(pagination.getNextItem().getPosition(), pagination.getNextItem().getItem());
        return gui;
    }

    static int getRealPosition(int pos, int page, int guiSize) {
        int min = page * (guiSize - 1);
        int max = min + guiSize - 1;
        if (pos >= min && pos <= max) return page > 0 ? pos - min - 1 : pos - min;
        return -1;
    }
}
