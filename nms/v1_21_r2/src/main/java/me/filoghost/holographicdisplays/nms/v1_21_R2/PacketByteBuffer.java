/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R2;

import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

class PacketByteBuffer {

    private static final ThreadLocal<PacketByteBuffer> LOCAL_INSTANCE = ThreadLocal.withInitial(PacketByteBuffer::new);

    private final RegistryFriendlyByteBuf serializer;

    static PacketByteBuffer get() {
        PacketByteBuffer instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

    private PacketByteBuffer() {
        this.serializer =
                new RegistryFriendlyByteBuf(Unpooled.buffer(), MinecraftServer.getDefaultRegistryAccess());
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
        serializer.c(i);
    }

    void writeVarIntArray(int i1) {
        writeVarInt(1);
        writeVarInt(i1);
    }

    void writeVarIntArray(int i1, int i2) {
        writeVarInt(2);
        writeVarInt(i1);
        writeVarInt(i2);
    }

    void writeUUID(UUID uuid) {
        serializer.a(uuid);
    }

    <T> void writeDataWatcherEntry(DataWatcherKey<T> key, T value) {
        serializer.writeByte(key.getIndex());
        writeVarInt(key.getSerializerTypeID());
        key.getSerializer().codec().encode(serializer, value);
    }

    void writeDataWatcherEntriesEnd() {
        serializer.writeByte(0xFF);
    }

    public RegistryFriendlyByteBuf getInternalSerializer() {
        return serializer;
    }

    void clear() {
        serializer.clear();
    }

}
