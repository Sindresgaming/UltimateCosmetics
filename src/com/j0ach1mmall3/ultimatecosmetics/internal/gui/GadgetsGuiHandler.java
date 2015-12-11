package com.j0ach1mmall3.ultimatecosmetics.internal.gui;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.GUI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.events.PlayerOpenGuiEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Gadgets;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Pagination;
import com.j0ach1mmall3.ultimatecosmetics.internal.gadgets.GadgetImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by j0ach1mmall3 on 17:51 3/10/2015 using IntelliJ IDEA.
 */
public final class GadgetsGuiHandler extends GuiHandler {
    public GadgetsGuiHandler(Main plugin) {
        super(plugin);
    }

    public static void open(Player p, int page) {
        Player p1 = p;
        Gadgets config = plugin.getGadgets();
        CosmeticsAPI api = plugin.getApi();
        GUI gui = buildGui(config.getGuiName(), config.getGuiSize());
        for (GadgetStorage gadget : config.getGadgets()) {
            int position = getRealPosition(gadget.getPosition(), page, config.getGuiSize());
            if (position != -1) {
                ItemStack item = gadget.getItem().clone();
                if (api.hasGadget(p1)) {
                    if (api.getGadget(p1).getIdentifier().equals(gadget.getIdentifier()))
                        item.addUnsafeEnchantment(glow, 1);
                }
                gui.setItem(position, item);
                if (config.isNoPermissionItemEnabled() && !Methods.hasPermission(p1, gadget.getPermission())) {
                    gui.setItem(position, config.getNoPermissionItem(gadget));
                }
            }
        }
        gui.setItem(config.getRemoveItemPosition(), config.getRemoveItem());
        PlayerOpenGuiEvent event = new PlayerOpenGuiEvent(p1, gui);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        gui = event.getGui();
        p1 = event.getPlayer();
        PAGEMAP.put(p1.getName(), page);
        gui.open(p1);
    }

    @Override
    protected void handleClick(GUI gui, Player p, ItemStack item) {
        Gadgets config = plugin.getGadgets();
        if (gui.getName().equals(Placeholders.parse(config.getGuiName(), p))) {
            Pagination pagination = plugin.getPagination();
            CosmeticsAPI api = plugin.getApi();
            if (config.getRemoveItem().isSimilar(item)) {
                if (api.hasGadget(p)) p.getInventory().setItem(config.getGadgetSlot(), null);
                Sounds.playSound(p, config.getRemoveSound());
                p.closeInventory();
                return;
            }
            if (Methods.isNoPermissionItem(config.getNoPermissionItem(), item)) {
                plugin.informPlayerNoPermission(p, config.getNoPermissionMessage());
                return;
            }
            if (pagination.getPreviousItem().getItem().isSimilar(item)) {
                if (plugin.getBabies().getGuiClickSound() != null)
                    Sounds.playSound(p, plugin.getBabies().getGuiClickSound());
                int currPage = PAGEMAP.get(p.getName());
                if (currPage == 0) {
                    open(p, config.getMaxPage());
                } else {
                    open(p, currPage - 1);
                }
                return;
            }
            if (pagination.getNextItem().getItem().isSimilar(item)) {
                if (plugin.getBabies().getGuiClickSound() != null)
                    Sounds.playSound(p, plugin.getBabies().getGuiClickSound());
                int currPage = PAGEMAP.get(p.getName());
                if (currPage == config.getMaxPage()) {
                    open(p, 0);
                } else {
                    open(p, currPage + 1);
                }
                return;
            }
            GadgetStorage gadget = api.getGadgetByItemStack(item);
            if (!Methods.hasPermission(p, gadget.getPermission())) {
                plugin.informPlayerNoPermission(p, config.getNoPermissionMessage());
                return;
            }
            new GadgetImpl(p, gadget).give();
            Sounds.playSound(p, config.getGiveSound());
            p.closeInventory();
        }
    }
}
