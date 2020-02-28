/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies
https://github.com/mooviies/CommandBlocksForEveryone

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/.
 */
package com.mooviies.cbforeveryone.registry;

import net.minecraft.block.Block;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("minecraft")
public class CBFEVanillaBlocks {
    private CBFEVanillaBlocks() {}

    @ObjectHolder("command_block")
    public static final CommandBlockBlock COMMAND_BLOCK = null;

    @ObjectHolder("chain_command_block")
    public static final CommandBlockBlock CHAIN_COMMAND_BLOCK = null;

    @ObjectHolder("repeating_command_block")
    public static final CommandBlockBlock REPEATING_COMMAND_BLOCK = null;

    public static void register(IForgeRegistry<Block> registry)
    {
        registry.registerAll(
                new CommandBlockBlock(Block.Properties.create(Material.IRON, MaterialColor.BROWN).hardnessAndResistance(5.0F, 3600000.0F).harvestTool(ToolType.PICKAXE))
                        .setRegistryName(new ResourceLocation("minecraft", "command_block")),
                new CommandBlockBlock(Block.Properties.create(Material.IRON, MaterialColor.GREEN).hardnessAndResistance(5.0F, 3600000.0F).harvestTool(ToolType.PICKAXE))
                        .setRegistryName(new ResourceLocation("minecraft", "chain_command_block")),
                new CommandBlockBlock(Block.Properties.create(Material.IRON, MaterialColor.PURPLE).hardnessAndResistance(5.0F, 3600000.0F).harvestTool(ToolType.PICKAXE))
                        .setRegistryName(new ResourceLocation("minecraft", "repeating_command_block"))
        );
    }
}
