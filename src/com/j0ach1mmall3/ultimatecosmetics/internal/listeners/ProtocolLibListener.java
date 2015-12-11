package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.j0ach1mmall3.ultimatecosmetics.Main;

/**
 * Created by j0ach1mmall3 on 2:27 4/10/2015 using IntelliJ IDEA.
 */
@SuppressWarnings("deprecation")
public final class ProtocolLibListener extends PacketAdapter {
    private final Main plugin;

    public ProtocolLibListener(Main plugin) {
        super(plugin, ConnectionSide.SERVER_SIDE, ListenerPriority.NORMAL, com.comphenix.protocol.Packets.Server.NAMED_SOUND_EFFECT);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        String sound = packet.getStrings().read(0);
        if ("mob.bat.idle".equals(sound) && (this.plugin.getBalloons().isDisableBatSounds() || this.plugin.getGadgets().isDisableBatSounds())) {
            event.setCancelled(true);
        }
    }
}
