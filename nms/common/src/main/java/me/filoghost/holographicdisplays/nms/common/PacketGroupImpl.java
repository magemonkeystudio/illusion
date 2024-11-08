/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class PacketGroupImpl implements PacketGroup {
    private final List<PacketGroup> packets;

    PacketGroupImpl(PacketGroup... packets) {
        this.packets = new LinkedList<>(Arrays.asList(packets));
    }

    @Override
    public void sendTo(Player player) {
        for (PacketGroup packet : packets) {
            packet.sendTo(player);
        }
    }
}
