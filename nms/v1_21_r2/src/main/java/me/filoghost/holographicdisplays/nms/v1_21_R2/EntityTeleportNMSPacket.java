/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R2;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3D;

import java.util.HashSet;
import java.util.Set;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, PositionCoordinates position, double positionOffsetY) {
        Vec3D                           pos                             = new Vec3D(position.getX(), position.getY() + positionOffsetY, position.getZ());
        Vec3D                           delta                           = new Vec3D(0, 0, 0);
        PositionMoveRotation            positionMoveRotation            = new PositionMoveRotation(pos, delta, 0, 0);
        Set<Relative>                   relatives                       = new HashSet<>();

        this.rawPacket = new PacketPlayOutEntityTeleport(entityID.getNumericID(), positionMoveRotation, relatives, false);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
