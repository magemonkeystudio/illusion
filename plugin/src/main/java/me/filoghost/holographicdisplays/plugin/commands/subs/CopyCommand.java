/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
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

public class CopyCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public CopyCommand(InternalHologramEditor hologramEditor) {
        super("copy");
        setMinArgs(2);
        setUsageArgs("<fromHologram> <toHologram>");
        setDescription("Copies the content from a hologram to another one, replacing it.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram fromHologram = hologramEditor.getExistingHologram(args[0]);
        InternalHologram toHologram   = hologramEditor.getExistingHologram(args[1]);

        toHologram.setLines(fromHologram.getLines());
        hologramEditor.saveChanges(toHologram, ChangeType.EDIT_LINES);

        sender.sendMessage(ColorScheme.PRIMARY + "Lines of hologram \"" + fromHologram.getName() + "\""
                + " copied to hologram \"" + toHologram.getName() + "\".");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 2) return Collections.emptyList();

        return filterStartingWith(args[args.length - 1], hologramEditor.getHolograms().stream().map(InternalHologram::getName).collect(Collectors.toList()));
    }

}
