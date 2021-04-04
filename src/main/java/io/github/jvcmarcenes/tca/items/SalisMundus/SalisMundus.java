package io.github.jvcmarcenes.tca.items.SalisMundus;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class SalisMundus extends Item {
    
  public SalisMundus(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    Block block = context.getWorld().getBlockState(context.getPos()).getBlock();

    Optional<Consumer<ItemUseContext>> effect = SalisMundusEffects.getEffect(block);

    if (effect.isPresent()) {
      effect.get().accept(context);

      if (!context.getPlayer().abilities.isCreativeMode)
        context.getItem().shrink(1);

      return ActionResultType.SUCCESS;
    } else {
      return ActionResultType.PASS;
    }
  }
}
