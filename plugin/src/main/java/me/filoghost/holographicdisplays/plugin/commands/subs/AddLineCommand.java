/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.filoghost.fcommons.command.CommandHelper.filterStartingWith;

public class AddLineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public AddLineCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("addLine");
        setMinArgs(2);
        setUsageArgs("<hologram> <text>");
        setDescription("Adds a line to a hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram       = hologramEditor.getExistingHologram(args[0]);
        String           serializedLine = Strings.joinFrom(" ", args, 1);

        InternalHologramLine line = hologramEditor.parseHologramLine(serializedLine);

        hologram.addLine(line);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        sender.sendMessage(ColorScheme.PRIMARY + "Line added.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Add";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> hologramNames
                    = hologramEditor.getHolograms().stream().map(InternalHologram::getName).collect(Collectors.toList());
            return filterStartingWith(args[0], hologramNames);
        }

        if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<text>");
        }
        
        return Collections.emptyList();
    }
}
