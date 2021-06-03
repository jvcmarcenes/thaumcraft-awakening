package io.github.jvcmarcenes.tca.items.EssentiaStorage;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class AspectPhial extends Item implements IAspectStorage {

  public AspectPhial(Properties properties) {
    super(properties);
  }

  @Override
  public int getStoredAmount(ItemStack stack) { return IAspectStorage.getStoredAspect(stack).equals(Aspects.NONE) ? 0 : 10; }

  @Override
  public int getStorageLeft(ItemStack stack) { return IAspectStorage.getStoredAspect(stack).equals(Aspects.NONE) ? 10 : 0; }

  @Override
  public boolean fillsPartially() { return false; }

  @Override
  public ItemStack create(String aspect, int amount, int count) {
    ItemStack stack = new ItemStack(ModItems.ASPECT_PHIAL.get(), count);

    if (aspect.equals(Aspects.NONE) || amount == 0) return stack;

    IAspectStorage.setStoredAspect(stack, aspect);
    return stack;
  }

  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    if (!isInGroup(group)) return;

    items.add(new ItemStack(ModItems.ASPECT_PHIAL.get()));

    for (String aspect : Aspects.ASPECTS) {
      CompoundNBT tag = new CompoundNBT();
      tag.putString(ASPECT_TAG, aspect);
      ItemStack stack = new ItemStack(ModItems.ASPECT_PHIAL.get());
      stack.setTag(tag);
      items.add(stack);
    }
  }
}
