/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v26_2;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

class EntitySpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntitySpawnNMSPacket(EntityID entityID,
                         EntityType<?> entityType,
                         PositionCoordinates position,
                         double positionOffsetY) {
        this.rawPacket = new ClientboundAddEntityPacket(
                entityID.getNumericID(),
                entityID.getUUID(),
                position.getX(),
                position.getY() + positionOffsetY,
                position.getZ(),
                // x-rot (yaw)
                0,
                // y-rot (pitch)
                0,
                // The entity type to spawn
                entityType,
                // Apply velocity? 1 if present and zero (otherwise by default a random velocity is applied)
                entityType == EntityTypeConstants.ITEM ? 1 : 0,
                // Velocity
                new Vec3(0, 0, 0),
                // y-head-rot (???)
                0
        );
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
