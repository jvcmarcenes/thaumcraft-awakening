package io.github.jvcmarcenes.tca.items.EssentiaStorage;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import net.minecraft.item.ItemStack;

public interface IAspectStorage {

  int getStoredAmount(ItemStack stack);
  int getStorageLeft(ItemStack stack);
  boolean fillsPartially();
  ItemStack create(String aspect, int amount, int count);

  public static final String ASPECT_TAG = "aspect";

  public static void setStoredAspect(ItemStack stack, String aspect) {
    assert stack.getItem() instanceof IAspectStorage;

    stack.getOrCreateTag().putString(ASPECT_TAG, aspect);
  }

  public static String getStoredAspect(ItemStack stack) {
    assert stack.getItem() instanceof IAspectStorage;

    return stack.hasTag() ? stack.getTag().getString(ASPECT_TAG) : Aspects.NONE;
  };
}
