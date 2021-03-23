package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraftforge.items.IItemHandler;

public class ArcaneCraftingInventory implements IInventory, IRecipeHelperPopulator {

    private final IItemHandler itemStackHandler;
    private final String aspect;

    private final int width;
    private final int height;

    public ArcaneCraftingInventory(IItemHandler itemStackHandler, String aspect, int width, int height) {
        this.itemStackHandler = itemStackHandler;
        this.aspect = aspect;
        this.width = width;
        this.height = height;
    }

    public String getAspect() { return aspect; }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public ItemStack getStackInSlot(int index) { return itemStackHandler.getStackInSlot(index); }

    public int getSizeInventory() { return itemStackHandler.getSlots(); }

    public boolean isEmpty() {
        for(int slot = 0; slot < itemStackHandler.getSlots(); slot++)
            if (!itemStackHandler.getStackInSlot(slot).isEmpty())
                return false;

        return true;
    }

    public ItemStack removeStackFromSlot(int index) { return ItemStack.EMPTY; }

    public ItemStack decrStackSize(int index, int count) { return ItemStack.EMPTY; }

    public void setInventorySlotContents(int index, ItemStack stack) {}

    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    public void markDirty() {}

    public void clear() {}

    public void fillStackedContents(RecipeItemHelper helper) {
        for (int slot = 0; slot < itemStackHandler.getSlots(); slot++)
            helper.accountPlainStack(itemStackHandler.getStackInSlot(slot));
    }
}
