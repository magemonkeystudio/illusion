/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_15_R1;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;

import java.io.IOException;
import java.util.UUID;

class PacketByteBuffer {

    private static final ThreadLocal<PacketByteBuffer> LOCAL_INSTANCE = ThreadLocal.withInitial(PacketByteBuffer::new);

    private final PacketDataSerializer serializer;

    static PacketByteBuffer get() {
        PacketByteBuffer instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

    private PacketByteBuffer() {
        this.serializer = new PacketDataSerializer(Unpooled.buffer());
    }

    void writeBoolean(boolean flag) {
        serializer.writeBoolean(flag);
    }

    void writeByte(int i) {
        serializer.writeByte(i);
    }

    void writeShort(int i) {
        serializer.writeShort(i);
    }

    void writeInt(int i) {
        serializer.writeInt(i);
    }

    void writeDouble(double d) {
        serializer.writeDouble(d);
    }

    void writeVarInt(int i) {
        serializer.d(i);
    }

    void writeVarIntArray(int i1) {
        writeVarInt(1);
        writeVarInt(i1);
    }

    void writeUUID(UUID uuid) {
        serializer.a(uuid);
    }

    <T> void writeDataWatcherEntry(DataWatcherKey<T> key, T value) {
        serializer.writeByte(key.getIndex());
        writeVarInt(key.getSerializerTypeID());
        key.getSerializer().a(serializer, value);
    }

    void writeDataWatcherEntriesEnd() {
        serializer.writeByte(0xFF);
    }

    void clear() {
        serializer.clear();
    }

    <T extends Packet<?>> T writeDataTo(T packet) {
        try {
            packet.a(serializer);
            return packet;
        } catch (IOException e) {
            // Never thrown by the implementations
            throw new RuntimeException(e);
        }
    }

}
