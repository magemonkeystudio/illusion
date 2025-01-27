/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.filoghost.fcommons.command.CommandHelper.filterStartingWith;


public class TeleportCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public TeleportCommand(InternalHologramEditor hologramEditor) {
        super("teleport", "tp");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Teleports to a hologram.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player           player   = CommandValidate.getPlayerSender(sender);
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        hologramEditor.teleportLookingDown(player, hologram.getPosition().toLocation());
        player.sendMessage(ColorScheme.PRIMARY + "Teleported to the hologram \"" + hologram.getName() + "\".");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) return Collections.emptyList();

        return filterStartingWith(args[args.length - 1], hologramEditor.getHolograms().stream().map(InternalHologram::getName).collect(Collectors.toList()));
    }

}
