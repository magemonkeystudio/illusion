/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R1;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;

class EntityDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityDestroyNMSPacket(EntityID entityID) {
        this.rawPacket = new PacketPlayOutEntityDestroy(entityID.getNumericID());
    }

    EntityDestroyNMSPacket(EntityID entityID1, EntityID entityID2) {
        this.rawPacket = new PacketPlayOutEntityDestroy(entityID1.getNumericID(), entityID2.getNumericID());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
