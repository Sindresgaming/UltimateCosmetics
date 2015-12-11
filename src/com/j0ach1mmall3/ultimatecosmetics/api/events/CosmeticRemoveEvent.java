package com.j0ach1mmall3.ultimatecosmetics.api.events;

import com.j0ach1mmall3.ultimatecosmetics.api.cosmetics.Cosmetic;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by j0ach1mmall3 on 17:54 13/10/2015 using IntelliJ IDEA.
 */
public class CosmeticRemoveEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private Cosmetic cosmetic;
    private boolean cancelled;

    public CosmeticRemoveEvent(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    public final Cosmetic getCosmetic() {
        return this.cosmetic;
    }

    public final void setCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
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
