package io.github.jvcmarcenes.tca.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CraftingOutputSlot extends SlotItemHandler {

    private final ICraftingTE tileEntity;

    public CraftingOutputSlot(IItemHandler itemHandler, int index, int x, int y, ICraftingTE tileEntity) {
        super(itemHandler, index, x, y);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return tileEntity.canCraft(player);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        tileEntity.craft();

        return super.onTake(player, stack);
    }
}
