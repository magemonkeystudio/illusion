/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.filoghost.fcommons.command.CommandHelper.filterStartingWith;

public class AlignCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public AlignCommand(InternalHologramEditor hologramEditor) {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologramToAlign> <referenceHologram>");
        setDescription("Aligns a hologram to another along the specified axis.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[1]);
        InternalHologram referenceHologram = hologramEditor.getExistingHologram(args[2]);

        CommandValidate.check(hologram != referenceHologram, "The holograms must not be the same.");

        Position referencePosition = referenceHologram.getPosition();
        Position oldPosition = hologram.getPosition();
        Position newPosition;

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            newPosition = Position.of(oldPosition.getWorldName(), referencePosition.getX(), oldPosition.getY(), oldPosition.getZ());
        } else if (axis.equalsIgnoreCase("y")) {
            newPosition = Position.of(oldPosition.getWorldName(), oldPosition.getX(), referencePosition.getY(), oldPosition.getZ());
        } else if (axis.equalsIgnoreCase("z")) {
            newPosition = Position.of(oldPosition.getWorldName(), oldPosition.getX(), oldPosition.getY(), referencePosition.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            newPosition = Position.of(oldPosition.getWorldName(), referencePosition.getX(), oldPosition.getY(), referencePosition.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.setPosition(newPosition);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_POSITION);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + hologram.getName() + "\""
                + " aligned to the hologram \"" + referenceHologram.getName() + "\""
                + " on the " + axis.toUpperCase() + " axis.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filterStartingWith(args[0], "X", "Y", "Z", "XZ");
        } else if (args.length == 2 || args.length == 3) {
            List<String> hologramNames = hologramEditor.getHolograms().stream().map(InternalHologram::getName).collect(Collectors.toList());
            return filterStartingWith(args[args.length - 1], hologramNames);
        } else {
            return Collections.emptyList();
        }
    }

}
