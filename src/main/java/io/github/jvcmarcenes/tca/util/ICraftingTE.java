package io.github.jvcmarcenes.tca.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ICraftingTE {

    boolean canCraft(PlayerEntity player);
    ItemStack craft();

}
