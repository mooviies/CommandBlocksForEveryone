package com.mooviies.cbforeveryone.registry;

import com.mooviies.cbforeveryone.CBFECreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("minecraft")
public class CBFEVanillaItems {
    private CBFEVanillaItems() {}

    @ObjectHolder("command_block")
    public static final BlockItem COMMAND_BLOCK = null;

    @ObjectHolder("chain_command_block")
    public static final BlockItem CHAIN_COMMAND_BLOCK = null;

    @ObjectHolder("repeating_command_block")
    public static final BlockItem REPEATING_COMMAND_BLOCK = null;

    public static void register(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                new BlockItem(Blocks.COMMAND_BLOCK, (
                        new Item.Properties()).rarity(Rarity.EPIC).group(CBFECreativeTabs.DEFAULT))
                        .setRegistryName(new ResourceLocation("minecraft", "command_block")),
                new BlockItem(Blocks.CHAIN_COMMAND_BLOCK, (
                        new Item.Properties()).rarity(Rarity.EPIC).group(CBFECreativeTabs.DEFAULT))
                        .setRegistryName(new ResourceLocation("minecraft", "chain_command_block")),
                new BlockItem(Blocks.REPEATING_COMMAND_BLOCK, (
                        new Item.Properties()).rarity(Rarity.EPIC).group(CBFECreativeTabs.DEFAULT))
                        .setRegistryName(new ResourceLocation("minecraft", "repeating_command_block"))
        );
    }
}
