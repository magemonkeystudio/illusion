/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.filoghost.fcommons.command.CommandHelper.filterStartingWith;

public class InfoCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public InfoCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("info", "details");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Lists the lines of a hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        DisplayFormat.sendTitle(sender, "Lines of the hologram \"" + hologram.getName() + "\"");
        int index = 0;

        for (InternalHologramLine line : hologram.getLines()) {
            index++;
            sender.sendMessage(ColorScheme.SECONDARY_BOLD + index + ColorScheme.SECONDARY_DARK + ". "
                    + ColorScheme.SECONDARY + line.getSerializedString());
        }
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "View";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) return Collections.emptyList();

        return filterStartingWith(args[args.length - 1], hologramEditor.getHolograms().stream().map(InternalHologram::getName).collect(Collectors.toList()));
    }

}
