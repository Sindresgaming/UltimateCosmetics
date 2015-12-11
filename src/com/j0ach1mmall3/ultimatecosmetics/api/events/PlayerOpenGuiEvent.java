package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.jlib.inventory.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by j0ach1mmall3 on 17:35 13/10/2015 using IntelliJ IDEA.
 */
public class PlayerOpenGuiEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private GUI gui;
    private boolean cancelled;

    public PlayerOpenGuiEvent(Player player, GUI gui) {
        this.player = player;
        this.gui = gui;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final void setPlayer(Player player) {
        this.player = player;
    }

    public final GUI getGui() {
        return this.gui;
    }

    public final void setGui(GUI gui) {
        this.gui = gui;
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
