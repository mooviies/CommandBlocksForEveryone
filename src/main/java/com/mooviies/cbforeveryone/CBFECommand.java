/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
