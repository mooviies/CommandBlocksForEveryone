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
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public final class CBFECommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("cbfe").requires((commandSource) -> { return commandSource.hasPermissionLevel(2); })
                .then(Commands.literal("global")
                        .then(Commands.literal("allow")
                                .executes((commandContext) -> { return setDimPermission(commandContext.getSource(), CBFEPermissionService.getGlobal(), true); })
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                EntityArgument.getPlayers(commandContext, "targets"), CBFEPermissionService.getGlobal(), true); })))
                        .then(Commands.literal("deny")
                                .executes((commandContext) -> { return setDimPermission(commandContext.getSource(), CBFEPermissionService.getGlobal(), false); })
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                EntityArgument.getPlayers(commandContext, "targets"), CBFEPermissionService.getGlobal(), false); })))
                        .then(Commands.literal("reset")
                                .executes((commandContext) -> { return resetDimPermission(commandContext.getSource(), CBFEPermissionService.getGlobal()); })
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes((commandContext) -> { return resetDimPermission(commandContext.getSource(),
                                                EntityArgument.getPlayers(commandContext, "targets"), CBFEPermissionService.getGlobal()); }))))
                .then(Commands.literal("world")
                        .then(Commands.argument("dimension", DimensionArgument.getDimension())
                                .then(Commands.literal("allow")
                                        .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId()), true); })
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                        EntityArgument.getPlayers(commandContext, "targets"),
                                                        CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId()), true); })))
                                .then(Commands.literal("deny")
                                        .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId()), false); })
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                        EntityArgument.getPlayers(commandContext, "targets"),
                                                        CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId()), true); })))
                                .then(Commands.literal("reset")
                                        .executes((commandContext) -> { return resetDimPermission(commandContext.getSource(),
                                                CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId())); })
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes((commandContext) -> { return setDimPermission(commandContext.getSource(),
                                                        EntityArgument.getPlayers(commandContext, "targets"),
                                                        CBFEPermissionService.get(DimensionArgument.func_212592_a(commandContext, "dimension").getId()), true); }))))));
    }

    public static int setDimPermission(CommandSource source, CBFEPermissionService permission, boolean allow)
    {
        permission.setWorldPermission(allow);
        if(permission == CBFEPermissionService.getGlobal())
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.set.global", allow ? "allow" : "deny"), true);
        else
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.set.dim", permission.dimension, allow ? "allow" : "deny"), true);
        return 1;
    }

    public static int setDimPermission(CommandSource source, Collection<ServerPlayerEntity> targetPlayers, CBFEPermissionService permission, boolean allow)
    {
        for(ServerPlayerEntity player : targetPlayers)
        {
            permission.setWorldPermission(player, allow);
        }

        if(targetPlayers.size() == 1)
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.set.single",
                    targetPlayers.iterator().next().getDisplayName(), permission.dimension, allow ? "allow" : "deny"),
                    true);
        }
        else
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.set.multiple",
                    targetPlayers.size(), permission.dimension, allow ? "allow" : "deny"), true);
        }

        return targetPlayers.size();
    }

    public static int resetDimPermission(CommandSource source, CBFEPermissionService permission)
    {
        permission.resetWorldPermission();
        if(permission == CBFEPermissionService.getGlobal())
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.reset.global"), true);
        else
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.reset.dim", permission.dimension, CBFEAPI.getPermission() ? "allow" : "deny"), true);
        return 1;
    }

    public static int resetDimPermission(CommandSource source, Collection<ServerPlayerEntity> targetPlayers, CBFEPermissionService permission)
    {
        for(ServerPlayerEntity player : targetPlayers)
        {
            permission.resetWorldPermission(player);
        }

        if(targetPlayers.size() == 1)
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.reset.single",
                            targetPlayers.iterator().next().getDisplayName(), permission.dimension, permission.getWorldPermission() ? "allow" : "deny"),
                    true);
        }
        else
        {
            source.sendFeedback(new TranslationTextComponent("command.cbfe.perm.reset.multiple",
                    targetPlayers.size(), permission.dimension, permission.getWorldPermission() ? "allow" : "deny"), true);
        }

        return targetPlayers.size();
    }
}
