/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v26_1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.nms.common.*;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VersionNMSManager implements NMSManager {

    private static final ReflectField<AtomicInteger> ENTITY_ID_COUNTER_FIELD =
            ReflectField.lookup(AtomicInteger.class, Entity.class, "ENTITY_COUNTER");
    private static final ReflectField<Connection>     NETWORK_MANAGER_FIELD  =
            ReflectField.lookup(Connection.class, ServerCommonPacketListenerImpl.class, "connection");
    private final        Supplier<Integer>            entityIDGenerator;

    public VersionNMSManager(ErrorCollector errorCollector) {
        this.entityIDGenerator = getEntityIDGenerator(errorCollector);

        // Force initialization of class to eventually throw exceptions early
        DataWatcherKey.ENTITY_STATUS.getIndex();
    }

    private Supplier<Integer> getEntityIDGenerator(ErrorCollector errorCollector) {
        try {
            AtomicInteger nmsEntityIDCounter = ENTITY_ID_COUNTER_FIELD.getStatic();
            return nmsEntityIDCounter::incrementAndGet;
        } catch (ReflectiveOperationException e) {
            errorCollector.add(e, NMSErrors.EXCEPTION_GETTING_ENTITY_ID_GENERATOR);
            return new FallbackEntityIDGenerator();
        }
    }

    private EntityID newEntityID() {
        return new EntityID(entityIDGenerator);
    }

    @Override
    public TextNMSPacketEntity newTextPacketEntity() {
        return new VersionTextNMSPacketEntity(newEntityID());
    }

    @Override
    public ItemNMSPacketEntity newItemPacketEntity() {
        return new VersionItemNMSPacketEntity(newEntityID(), newEntityID());
    }

    @Override
    public ClickableNMSPacketEntity newClickablePacketEntity() {
        return new VersionClickableNMSPacketEntity(newEntityID());
    }

    @Override
    public void injectPacketListener(Player player, PacketListener packetListener) {
        modifyPipeline(player, (ChannelPipeline pipeline) -> {
            ChannelHandler currentListener = pipeline.get(InboundPacketHandler.HANDLER_NAME);
            if (currentListener != null) {
                pipeline.remove(InboundPacketHandler.HANDLER_NAME);
            }
            pipeline.addBefore("packet_handler",
                    InboundPacketHandler.HANDLER_NAME,
                    new InboundPacketHandler(player, packetListener));
        });
    }

    @Override
    public void uninjectPacketListener(Player player) {
        modifyPipeline(player, (ChannelPipeline pipeline) -> {
            ChannelHandler currentListener = pipeline.get(InboundPacketHandler.HANDLER_NAME);
            if (currentListener != null) {
                pipeline.remove(InboundPacketHandler.HANDLER_NAME);
            }
        });
    }

    /*
     * Modifying the pipeline in the main thread can cause deadlocks, delays and other concurrency issues,
     * which can be avoided by using the event loop. Thanks to ProtocolLib for this insight.
     */
    private void modifyPipeline(Player player, Consumer<ChannelPipeline> pipelineModifierTask) {
        ServerGamePacketListenerImpl playerConnection = ((CraftPlayer) player).getHandle().connection;
        Connection networkManager;
        try {
            networkManager = NETWORK_MANAGER_FIELD.get(playerConnection);
        } catch (ReflectiveOperationException e) {
            Log.warning(NMSErrors.EXCEPTION_MODIFYING_CHANNEL_PIPELINE, e);
            return;
        }
        Channel channel = networkManager.channel;
        if (channel == null) {
            return;
        }
        EventLoop eventLoop = channel.eventLoop();

        Runnable safeModifierTask = () -> {
            if (!player.isOnline()) {
                return;
            }
            try {
                pipelineModifierTask.accept(channel.pipeline());
            } catch (Exception e) {
                Log.warning(NMSErrors.EXCEPTION_MODIFYING_CHANNEL_PIPELINE, e);
            }
        };

        if (eventLoop.inEventLoop()) {
            safeModifierTask.run();
        } else {
            eventLoop.execute(safeModifierTask);
        }
    }

}
