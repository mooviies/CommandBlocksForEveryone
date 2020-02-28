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
import com.mooviies.cbforeveryone.registry.CBFEVanillaItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class CBFEEventsHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
        CBFECommand.register(commandDispatcher);
    }

    @SubscribeEvent
    public static void onEntityPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof PlayerEntity))
            return;

        PlayerEntity playerEntity = (PlayerEntity) entity;
        Block blockAgainst = event.getPlacedAgainst().getBlock();

        Block block = event.getPlacedBlock().getBlock();
        if (block != Blocks.COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK)
            return;

        if (playerEntity.world.isRemote)
        {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)playerEntity;
            if(!serverPlayerEntity.server.isCommandBlockEnabled())
                return;
        }

        if(!playerEntity.isCrouching() && (blockAgainst == Blocks.COMMAND_BLOCK || blockAgainst == Blocks.CHAIN_COMMAND_BLOCK || blockAgainst == Blocks.REPEATING_COMMAND_BLOCK))
        {
            event.setCanceled(true);
            return;
        }

        BlockPos pos = event.getPos();
        String playerName = event.getEntity().getDisplayName().getString();
        String blockName = new TranslationTextComponent(block.getTranslationKey()).getString();

        if(!CBFEState.canUseCommandblock(playerEntity))
        {
            event.setCanceled(true);
            LOGGER.info(new TranslationTextComponent("event.cbfe.block.placed.deny", playerName, blockName, pos.getX(), pos.getY(), pos.getZ()).getString());
        }
        else
        {
            LOGGER.info(new TranslationTextComponent("event.cbfe.block.placed.allow", playerName, blockName, pos.getX(), pos.getY(), pos.getZ()).getString());
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        World world = event.getWorld();
        PlayerEntity playerEntity = event.getPlayer();

        if(world.isRemote || event.getHand() != Hand.MAIN_HAND || playerEntity.isCreative())
            return;

        if(!CBFEState.canUseCommandblock(playerEntity))
            return;

        BlockPos pos = event.getPos();

        if(playerEntity.getHeldItemMainhand().canDestroy(world.getTags(), new CachedBlockInfo(world, pos, false)))
        {
            if(world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS))
                Block.spawnAsEntity(world, pos, new ItemStack(world.getBlockState(pos).getBlock()));

            world.destroyBlock(pos, true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if(event.getHand() != Hand.MAIN_HAND)
            return;

        PlayerEntity playerEntity = event.getPlayer();
        if(playerEntity == null)
            return;

        BlockPos pos = event.getPos();
        String playerName = playerEntity.getDisplayName().getString();
        String blockName = new TranslationTextComponent(event.getWorld().getBlockState(pos).getBlock().getTranslationKey()).getString();

        if(!CBFEState.canUseCommandblock(playerEntity))
        {
            if(!event.getWorld().isRemote)
                LOGGER.info(new TranslationTextComponent("event.cbfe.block.edit.deny", playerName, blockName, pos.getX(), pos.getY(), pos.getZ()).getString());
            return;
        }

        TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
        if(!(tileEntity instanceof CommandBlockTileEntity))
            return;

        boolean isRemote = event.getWorld().isRemote;

        if(!isRemote)
        {
            LOGGER.info(new TranslationTextComponent("event.cbfe.block.edit.allow", playerName, blockName, pos.getX(), pos.getY(), pos.getZ()).getString());
        }

        if(isRemote)
        {
            playerEntity.openCommandBlock((CommandBlockTileEntity)tileEntity);
            return;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerEntity;
        if(!serverPlayer.server.isCommandBlockEnabled())
            return;

        if(!(serverPlayer.connection instanceof CBFEServerPlayNetHandler))
            serverPlayer.connection = new CBFEServerPlayNetHandler(serverPlayer);

        serverPlayer.openCommandBlock((CommandBlockTileEntity)tileEntity);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            //CBFEVanillaBlocks.register(blockRegistryEvent.getRegistry());
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            CBFEVanillaItems.register(itemRegistryEvent.getRegistry());
        }
    }
}
