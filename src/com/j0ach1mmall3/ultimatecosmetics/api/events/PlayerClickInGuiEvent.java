package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.jlib.inventory.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by j0ach1mmall3 on 17:35 13/10/2015 using IntelliJ IDEA.
 */
public class PlayerClickInGuiEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private GUI gui;
    private ItemStack clickedItem;
    private boolean cancelled;

    public PlayerClickInGuiEvent(Player player, GUI gui, ItemStack clickedItem) {
        super(player);
        this.gui = gui;
        this.clickedItem = clickedItem;
    }


    public final GUI getGui() {
        return this.gui;
    }

    public final void setGui(GUI gui) {
        this.gui = gui;
    }

    public final void setPlayer(Player player) {
        this.player = player;
    }

    public final ItemStack getClickedItem() {
        return this.clickedItem;
    }

    public final void setClickedItem(ItemStack clickedItem) {
        this.clickedItem = clickedItem;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public final HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
