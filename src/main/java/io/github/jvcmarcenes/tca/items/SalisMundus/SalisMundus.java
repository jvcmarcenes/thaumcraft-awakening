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

    if (SalisMundusEffects.hasActivator(block)) {
      SalisMundusEffects.callEffect(block, context);

      if (!context.getPlayer().abilities.isCreativeMode)
        context.getPlayer().getHeldItem(context.getHand()).shrink(1);

      return ActionResultType.SUCCESS;
    }
    
    // for (ISalisMundusTrigger trigger : ISalisMundusTrigger.triggers) {
    //   if (!trigger.validate(world, player, pos)) continue;

    //   trigger.execute(world, player, pos, context);
      
    //   if (!player.abilities.isCreativeMode)
    //     player.getHeldItem(context.getHand()).shrink(1);

    //   return ActionResultType.SUCCESS;
    // }
    
    return super.onItemUse(context);
  }
}
