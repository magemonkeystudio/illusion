/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v26_2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.nms.common.NMSErrors;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.entity.Player;

class InboundPacketHandler extends ChannelInboundHandlerAdapter {

    public static final String HANDLER_NAME = "holographic_displays_listener";

    private final Player player;
    private final PacketListener packetListener;

    InboundPacketHandler(Player player, PacketListener packetListener) {
        this.player = player;
        this.packetListener = packetListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        try {
            if (packet instanceof ServerboundInteractPacket) {
                int entityID = ((ServerboundInteractPacket) packet).entityId();
                boolean cancel = packetListener.onAsyncEntityInteract(player, entityID);
                if (cancel) {
                    return;
                }
            }
        } catch (Throwable t) {
            Log.warning(NMSErrors.EXCEPTION_ON_PACKET_READ, t);
        }
        super.channelRead(context, packet);
    }

}
