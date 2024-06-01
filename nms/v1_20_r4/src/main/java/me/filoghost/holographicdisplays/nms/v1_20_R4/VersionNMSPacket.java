/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R4;

import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

abstract class VersionNMSPacket implements PacketGroup {

    @Override
    public void sendTo(Player player) {
        ((CraftPlayer) player).getHandle().c.b(getRawPacket());
    }

    abstract Packet<?> getRawPacket();

}
