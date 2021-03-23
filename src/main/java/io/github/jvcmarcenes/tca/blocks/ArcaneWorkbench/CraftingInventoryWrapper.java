package io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraftforge.items.IItemHandler;

public class CraftingInventoryWrapper extends CraftingInventory {

    private final IItemHandler itemStackHandler;

    public CraftingInventoryWrapper(IItemHandler itemStackHandler, int width, int height) {
        super(null, width, height);

        this.itemStackHandler = itemStackHandler;
    }

    @Override public ItemStack getStackInSlot(int index) { return itemStackHandler.getStackInSlot(index); }

    //

    @Override public int getSizeInventory() { return itemStackHandler.getSlots(); }

    @Override
    public boolean isEmpty() {
        for(int slot = 0; slot < itemStackHandler.getSlots(); slot++) {
            if (!itemStackHandler.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override public ItemStack removeStackFromSlot(int index) { return ItemStack.EMPTY; }

    @Override public ItemStack decrStackSize(int index, int count) { return ItemStack.EMPTY; }

    @Override public void setInventorySlotContents(int index, ItemStack stack) {}

    @Override public void markDirty() {}

    @Override public void clear() {}

    @Override public void fillStackedContents(RecipeItemHelper helper) {}

}
