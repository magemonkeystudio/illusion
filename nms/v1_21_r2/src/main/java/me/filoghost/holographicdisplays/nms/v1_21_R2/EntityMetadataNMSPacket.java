/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R2;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;

import java.util.List;

class EntityMetadataNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    private EntityMetadataNMSPacket(EntityID entityId, List<DataWatcher.c<?>> watchers) {
        this.rawPacket = new PacketPlayOutEntityMetadata(entityId.getNumericID(), watchers);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

    public static DataWatcherPacketBuilder<EntityMetadataNMSPacket> builder(EntityID entityID) {
        return new Builder(entityID);
    }


    private static class Builder extends DataWatcherPacketBuilder<EntityMetadataNMSPacket> {
        public Builder(EntityID entityId) {
            super(entityId);
        }

        @Override
        EntityMetadataNMSPacket createPacket() {
            return new EntityMetadataNMSPacket(entityId, watchers);
        }
    }

}
