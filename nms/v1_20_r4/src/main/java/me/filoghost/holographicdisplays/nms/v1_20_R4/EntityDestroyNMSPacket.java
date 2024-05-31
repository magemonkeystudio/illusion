/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R4;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;

import java.lang.reflect.Constructor;

class EntityDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityDestroyNMSPacket(EntityID entityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarIntArray(entityID.getNumericID());

        try {
            Constructor<PacketPlayOutEntityDestroy> ctor =
                    PacketPlayOutEntityDestroy.class.getDeclaredConstructor(PacketDataSerializer.class);
            ctor.setAccessible(true);
            this.rawPacket = ctor.newInstance(packetByteBuffer.getInternalSerializer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntityDestroyNMSPacket", e);
        }
    }

    EntityDestroyNMSPacket(EntityID entityID1, EntityID entityID2) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarIntArray(entityID1.getNumericID(), entityID2.getNumericID());

        try {
            Constructor<PacketPlayOutEntityDestroy> ctor =
                    PacketPlayOutEntityDestroy.class.getDeclaredConstructor(PacketDataSerializer.class);
            ctor.setAccessible(true);
            this.rawPacket = ctor.newInstance(packetByteBuffer.getInternalSerializer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntityDestroyNMSPacket", e);
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
