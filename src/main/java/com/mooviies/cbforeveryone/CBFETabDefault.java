/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies
https://github.com/mooviies/CommandBlocksForEveryone

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/.
 */
package com.mooviies.cbforeveryone;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CBFETabDefault extends ItemGroup {
    public CBFETabDefault() {
        super(CBFE.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.COMMAND_BLOCK);
    }
}
