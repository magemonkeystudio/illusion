/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;

import java.lang.reflect.Constructor;

class EntitySpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntitySpawnNMSPacket(EntityID entityID, int entityTypeID, PositionCoordinates position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeUUID(entityID.getUUID());
        packetByteBuffer.writeVarInt(entityTypeID);

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // Object data
        if (entityTypeID == EntityTypeID.ITEM) {
            packetByteBuffer.writeInt(1); // Velocity is present and zero (otherwise by default a random velocity is applied)
        } else {
            packetByteBuffer.writeInt(0);
        }

        // Velocity
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);

        try {
            Constructor<PacketPlayOutSpawnEntity> ctor = PacketPlayOutSpawnEntity.class
                    .getDeclaredConstructor(RegistryFriendlyByteBuf.class);
            ctor.setAccessible(true);
            this.rawPacket = ctor.newInstance(packetByteBuffer.getInternalSerializer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntitySpawnNMSPacket", e);
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
