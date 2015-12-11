package com.j0ach1mmall3.ultimatecosmetics.internal.listeners;

import com.comphenix.tinyprotocol.TinyProtocol;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by j0ach1mmall3 on 12:35 6/09/2015 using IntelliJ IDEA.
 */
public final class ProtocolListener extends TinyProtocol {
    private final Main plugin;

    public ProtocolListener(Main plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public Object onPacketOutAsync(Player reciever, Channel channel, Object packet) {
        if (ReflectionAPI.getNmsClass("PacketPlayOutNamedSoundEffect").isInstance(packet)) {
            try {
                Field f = packet.getClass().getDeclaredField("a");
                f.setAccessible(true);
                String name = (String) f.get(packet);
                if ("mob.bat.idle".equals(name) && (this.plugin.getBalloons().isDisableBatSounds() || this.plugin.getGadgets().isDisableBatSounds()))
                    return null;
            } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return packet;
        }
        return super.onPacketOutAsync(reciever, channel, packet);
    }
}
