package io.github.jvcmarcenes.tca.items.SalisMundus;

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

    SalisMundusEffects.getEffect(block).ifPresent(effect -> {
      effect.accept(context);

      if (!context.getPlayer().abilities.isCreativeMode) 
        context.getItem().shrink(1);
    });

    return ActionResultType.SUCCESS;
  }
}
