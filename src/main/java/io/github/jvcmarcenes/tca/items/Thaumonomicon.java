package io.github.jvcmarcenes.tca.items;

import io.github.jvcmarcenes.tca.client.gui.ThaumonomiconScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Thaumonomicon extends Item {

  public Thaumonomicon(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);

    if (!world.isRemote) return ActionResult.resultSuccess(stack);

    Minecraft.getInstance().displayGuiScreen(new ThaumonomiconScreen(player));

    return ActionResult.resultSuccess(player.getHeldItem(hand));
  }
  
}
