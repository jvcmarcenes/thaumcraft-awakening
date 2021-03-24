package io.github.jvcmarcenes.tca.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface IAspectStorage {

  int getStoredAmount();

  public static final String ASPECT_TAG = "aspect";

  public static void setStoredAspect(ItemStack stack, String aspect) {
    assert stack.getItem() instanceof IAspectStorage;

    CompoundNBT tag = new CompoundNBT();
    tag.putString(ASPECT_TAG, aspect);
    stack.setTag(tag);
  }

  public static String getStoredAspect(ItemStack stack) {
    assert stack.getItem() instanceof IAspectStorage;

    if (stack.isEmpty()) return Aspects.NONE;
    return stack.hasTag() ? stack.getTag().getString(ASPECT_TAG) : Aspects.NONE;
  };
}
