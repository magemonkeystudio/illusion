/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v26_2;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;

// Sourced from net.minecraft.world.entity.EntityTypes
class EntityTypeConstants {

    static final EntityType<ArmorStand> ARMOR_STAND = EntityTypes.ARMOR_STAND;
    static final EntityType<ItemEntity> ITEM        = EntityTypes.ITEM;
    static final EntityType<Slime>      SLIME       = EntityTypes.SLIME;

}
