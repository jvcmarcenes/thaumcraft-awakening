package io.github.jvcmarcenes.tca.recipe.Alchemy;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlchemyRecipeInventory implements IInventory {

  private final AspectGroup aspects;
  private final Item catalyst;

  public AlchemyRecipeInventory(AspectGroup aspects, Item catalyst) {
    this.aspects = aspects;
    this.catalyst = catalyst;
  }

  public int getAmountOfAspect(String aspect) {
    return aspects.getAspectAmount(aspect);
  }

  public Item getCatalyst() {
    return catalyst;
  }

  public void clear() { }

  public int getSizeInventory() { return 0; }

  public boolean isEmpty() { return aspects.hasNone(); }

  public ItemStack getStackInSlot(int index) { return ItemStack.EMPTY; }

  public ItemStack decrStackSize(int index, int count) { return ItemStack.EMPTY; }

  public ItemStack removeStackFromSlot(int index) { return ItemStack.EMPTY; }

  public void setInventorySlotContents(int index, ItemStack stack) { }

  public void markDirty() { }

  public boolean isUsableByPlayer(PlayerEntity player) { return true; }
  
}
