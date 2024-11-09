/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

public interface PacketGroup {
    void sendTo(Player player);

    static PacketGroup of(PacketGroup ... packets) {
        return new PacketGroupImpl(packets);
    }
}
