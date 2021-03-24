package io.github.jvcmarcenes.tca.items;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.alchemy.IAspectStorage;
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
  public int getStoredAmount() {
    return 10;
  }

  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    if (!isInGroup(group)) return;

    CompoundNBT tag = new CompoundNBT();
    tag.putString(ASPECT_TAG, Aspects.NONE);
    ItemStack stack = new ItemStack(this);
    stack.setTag(tag);
    items.add(stack);

    // for (String aspect : Aspects.ASPECTS) {
    //   CompoundNBT tag = new CompoundNBT();
    //   tag.putString(ASPECT_TAG, aspect);
    //   ItemStack stack = new ItemStack(this);
    //   stack.setTag(tag);
    //   items.add(stack);
    // }
  }
}
