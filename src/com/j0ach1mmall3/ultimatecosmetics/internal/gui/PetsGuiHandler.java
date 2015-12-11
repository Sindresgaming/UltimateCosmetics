package com.j0ach1mmall3.ultimatecosmetics.internal.gui;

import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.inventory.GUI;
import com.j0ach1mmall3.jlib.methods.Sounds;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.events.PlayerOpenGuiEvent;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.PetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Pagination;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.Pets;
import com.j0ach1mmall3.ultimatecosmetics.internal.pets.PetImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by j0ach1mmall3 on 17:51 3/10/2015 using IntelliJ IDEA.
 */
public final class PetsGuiHandler extends GuiHandler {
    public PetsGuiHandler(Main plugin) {
        super(plugin);
    }

    public static void open(Player p, int page) {
        Player p1 = p;
        Pets config = plugin.getPets();
        CosmeticsAPI api = plugin.getApi();
        GUI gui = buildGui(config.getGuiName(), config.getGuiSize());
        for (PetStorage pet : config.getPets()) {
            int position = getRealPosition(pet.getPosition(), page, config.getGuiSize());
            if (position != -1) {
                ItemStack item = pet.getItem().clone();
                if (api.hasPet(p1)) {
                    if (api.getPet(p1).getPetStorage().getIdentifier().equals(pet.getIdentifier()))
                        item.addUnsafeEnchantment(glow, 1);
                }
                gui.setItem(position, item);
                if (config.isNoPermissionItemEnabled() && !Methods.hasPermission(p1, pet.getPermission())) {
                    gui.setItem(position, config.getNoPermissionItem(pet));
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
        Pets config = plugin.getPets();
        if (gui.getName().equals(Placeholders.parse(config.getGuiName(), p))) {
            Pagination pagination = plugin.getPagination();
            CosmeticsAPI api = plugin.getApi();
            if (config.getRemoveItem().isSimilar(item)) {
                if (api.hasPet(p)) api.getPet(p).remove();
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
            PetStorage pet = api.getPetByItemStack(item);
            if (!Methods.hasPermission(p, pet.getPermission())) {
                plugin.informPlayerNoPermission(p, config.getNoPermissionMessage());
                return;
            }
            new PetImpl(p, pet).give();
            Sounds.playSound(p, config.getGiveSound());
            p.closeInventory();
        }
    }
}
