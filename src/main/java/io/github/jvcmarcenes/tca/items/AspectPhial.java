package io.github.jvcmarcenes.tca.items;

import io.github.jvcmarcenes.tca.alchemy.IAspectStorage;
import net.minecraft.item.Item;

public class AspectPhial extends Item implements IAspectStorage {

  public AspectPhial(Properties properties) {
    super(properties);
  }

  @Override
  public int getStoredAmount() {
    return 10;
  }
}
