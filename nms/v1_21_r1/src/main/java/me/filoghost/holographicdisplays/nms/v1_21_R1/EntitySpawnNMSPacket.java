/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;

class EntitySpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntitySpawnNMSPacket(EntityID entityID,
                         EntityTypes<?> entityType,
                         PositionCoordinates position,
                         double positionOffsetY) {
        this.rawPacket = new PacketPlayOutSpawnEntity(
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
                entityType == EntityType.ITEM ? 1 : 0,
                // Velocity
                new Vec3D(0, 0, 0),
                // y-head-rot (???)
                0
        );
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
