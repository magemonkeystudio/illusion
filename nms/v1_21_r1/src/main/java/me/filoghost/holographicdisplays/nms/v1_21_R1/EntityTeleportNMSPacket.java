/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;

import java.lang.reflect.Constructor;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, PositionCoordinates position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // On ground
        packetByteBuffer.writeBoolean(false);

        try {
            Constructor<PacketPlayOutEntityTeleport> ctor =
                    PacketPlayOutEntityTeleport.class.getDeclaredConstructor(PacketDataSerializer.class);
            ctor.setAccessible(true);
            this.rawPacket = ctor.newInstance(packetByteBuffer.getInternalSerializer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntityTeleportNMSPacket", e);
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
