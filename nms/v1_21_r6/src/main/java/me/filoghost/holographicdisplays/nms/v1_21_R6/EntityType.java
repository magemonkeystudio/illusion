/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R6;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntitySlime;

// Sourced from net.minecraft.world.entity.EntityTypes
class EntityType {

    static final EntityTypes<EntityArmorStand> ARMOR_STAND = EntityTypes.h;
    static final EntityTypes<EntityItem>       ITEM        = EntityTypes.au;
    static final EntityTypes<EntitySlime>      SLIME       = EntityTypes.bm;

}
