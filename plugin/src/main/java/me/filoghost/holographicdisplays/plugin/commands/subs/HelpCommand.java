/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends HologramSubCommand {

    private final HologramCommandManager commandManager;

    public HelpCommand(HologramCommandManager commandManager) {
        super("help");
        setShowInHelpCommand(false);
        setDescription("Lists the main commands with a description.");

        this.commandManager = commandManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        DisplayFormat.sendTitle(sender, "Holographic Displays Commands");
        for (HologramSubCommand subCommand : commandManager.getSubCommands()) {
            if (subCommand.isShowInHelpCommand()) {
                DisplayFormat.sendCommandDescription(
                        sender,
                        subCommand.getFullUsageText(context),
                        subCommand.getDescription(context));
            }
        }

        if (sender instanceof Player) {
            DisplayFormat.sendHoverCommandDescriptionTip((Player) sender);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
