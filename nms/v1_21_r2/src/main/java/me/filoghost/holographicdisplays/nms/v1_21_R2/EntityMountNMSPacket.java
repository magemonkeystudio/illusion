/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R2;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutMount;

import java.lang.reflect.Constructor;

class EntityMountNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityMountNMSPacket(EntityID vehicleEntityID, EntityID passengerEntityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        // We'll leave this as a byte buffer for now, since I think the PacketPlayOutMount constructor
        // requires modifying the vehicle entity to have the passenger before we can create this packet
        packetByteBuffer.writeVarInt(vehicleEntityID.getNumericID());
        packetByteBuffer.writeVarIntArray(passengerEntityID.getNumericID());

        try {
            Constructor<PacketPlayOutMount> ctor =
                    PacketPlayOutMount.class.getDeclaredConstructor(PacketDataSerializer.class);
            ctor.setAccessible(true);
            this.rawPacket = ctor.newInstance(packetByteBuffer.getInternalSerializer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntityMountNMSPacket", e);
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
