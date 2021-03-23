package io.github.jvcmarcenes.tca.blocks.Crucible;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.alchemy.IAspectStorage;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class Crucible extends Block {

  public Crucible() {
    super(
      AbstractBlock.Properties.create(Material.IRON)
        .hardnessAndResistance(3, 10)
        .harvestTool(ToolType.PICKAXE)
        .sound(SoundType.METAL)
    );
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return ModTileEntityTypes.CRUCIBLE.get().create();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (world.isRemote) return ActionResultType.SUCCESS;

    CrucibleTE te = (CrucibleTE)world.getTileEntity(pos);
    ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

    if (stack.getItem() instanceof IAspectStorage && IAspectStorage.getStoredAspect(stack) == Aspects.NONE) {
      int amount = ((IAspectStorage)stack.getItem()).getStoredAmount();
      String aspect = te.aspects.getRandomAspectMin(amount);
      ItemStack newStack = new ItemStack(stack.getItem(), 1);
      IAspectStorage.setStoredAspect(newStack, aspect);
      te.aspects.drain(aspect, amount);
      player.inventory.addItemStackToInventory(newStack);
    } else {
      te.meltItem(player.getHeldItem(Hand.MAIN_HAND), player);
    }

    player.getHeldItem(Hand.MAIN_HAND).shrink(1);

    return ActionResultType.SUCCESS;
  }
}
