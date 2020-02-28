/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies
https://github.com/mooviies/CommandBlocksForEveryone

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/.
 */
package com.mooviies.cbforeveryone;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class CBFECommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("cbfe").requires((commandSource) -> { return commandSource.hasPermissionLevel(2); })
                .then(Commands.literal("allow")
                        .then(Commands.literal("global")
                                .executes((commandContext) -> { return changeAll(commandContext.getSource(), true); }))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes((commandContext) -> { return changePlayers(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), true); })))
                .then(Commands.literal("deny")
                        .then(Commands.literal("global")
                                .executes((commandContext) -> { return changeAll(commandContext.getSource(), false); }))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes((commandContext) -> { return changePlayers(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), false); })))
                .then(Commands.literal("clear")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes((commandContext) -> { return clearPlayers(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets")); }))));
    }

    public static int changeAll(CommandSource source, boolean allow)
    {
        CBFEState.setGlobalPrivilege(allow);
        source.sendFeedback(new TranslationTextComponent(allow ? "command.cbfe.allow.success.global" : "command.cbfe.deny.success.global"), true);
        return 1;
    }

    public static int changePlayers(CommandSource source, Collection<ServerPlayerEntity> targetPlayers, boolean allow)
    {
        for(ServerPlayerEntity player : targetPlayers)
        {
            CBFEState.setPrivilege(player.getUniqueID(), allow);
        }

        if(targetPlayers.size() == 1)
        {
            source.sendFeedback(new TranslationTextComponent(allow ? "command.cbfe.allow.success.single" : "command.cbfe.deny.success.single",
                    targetPlayers.iterator().next().getDisplayName()), true);
        }
        else
        {
            source.sendFeedback(new TranslationTextComponent(allow ? "command.cbfe.allow.success.multiple" : "command.cbfe.deny.success.multiple",
                    targetPlayers.size()), true);
        }

        return targetPlayers.size();
    }

    public static int clearPlayers(CommandSource source, Collection<ServerPlayerEntity> targetPlayers)
    {
        for(ServerPlayerEntity player : targetPlayers)
        {
            CBFEState.clearPrivilege(player.getUniqueID());
        }

        if(targetPlayers.size() == 1)
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.clear.success.single",
                    targetPlayers.iterator().next().getDisplayName()), true);
        }
        else
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.clear.success.multiple",
                    targetPlayers.size()), true);
        }

        return targetPlayers.size();
    }
}
