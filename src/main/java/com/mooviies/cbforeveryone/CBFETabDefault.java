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
