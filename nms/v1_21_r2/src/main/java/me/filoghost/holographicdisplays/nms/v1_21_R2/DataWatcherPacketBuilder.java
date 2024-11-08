/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_21_R2;

import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract class DataWatcherPacketBuilder<T> {

    private static final int MAX_CUSTOM_NAME_LENGTH = 5000;

    protected final EntityID               entityId;
    protected       List<DataWatcher.c<?>> watchers = new ArrayList<>();

    public DataWatcherPacketBuilder(EntityID entityId) {
        this.entityId = entityId;
    }

    DataWatcherPacketBuilder<T> setInvisible() {
        watchers.add(new DataWatcher.c<>(DataWatcherKey.ENTITY_STATUS.getIndex(),
                DataWatcherKey.ENTITY_STATUS.getSerializer(),
                (byte) 0x20));
        return this;
    }

    DataWatcherPacketBuilder<T> setArmorStandMarker() {
        setInvisible();
        // Small, no gravity, no base plate, marker
        watchers.add(new DataWatcher.c<>(DataWatcherKey.ARMOR_STAND_STATUS.getIndex(),
                DataWatcherKey.ARMOR_STAND_STATUS.getSerializer(),
                (byte) (0x01 | 0x02 | 0x08 | 0x10)));
        return this;
    }

    DataWatcherPacketBuilder<T> setCustomName(String customName) {
        watchers.add(new DataWatcher.c<>(DataWatcherKey.CUSTOM_NAME.getIndex(),
                DataWatcherKey.CUSTOM_NAME.getSerializer(),
                getCustomNameDataWatcherValue(customName)));
        watchers.add(new DataWatcher.c<>(DataWatcherKey.CUSTOM_NAME_VISIBILITY.getIndex(),
                DataWatcherKey.CUSTOM_NAME_VISIBILITY.getSerializer(),
                !Strings.isEmpty(customName)));
        return this;
    }

    private Optional<IChatBaseComponent> getCustomNameDataWatcherValue(String customName) {
        customName = Strings.truncate(customName, MAX_CUSTOM_NAME_LENGTH);
        if (!Strings.isEmpty(customName)) {
            return Optional.of(CraftChatMessage.fromString(customName, false, true)[0]);
        } else {
            return Optional.empty();
        }
    }

    DataWatcherPacketBuilder<T> setItemStack(ItemStack itemStack) {
        watchers.add(new DataWatcher.c<>(DataWatcherKey.ITEM_STACK.getIndex(),
                DataWatcherKey.ITEM_STACK.getSerializer(),
                CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    DataWatcherPacketBuilder<T> setSlimeSmall() {
        watchers.add(new DataWatcher.c<>(DataWatcherKey.SLIME_SIZE.getIndex(),
                DataWatcherKey.SLIME_SIZE.getSerializer(),
                0));
        return this;
    }

    T build() {
        return createPacket();
    }

    abstract T createPacket();

}
