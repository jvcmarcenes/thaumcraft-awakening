package io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench;

import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class ArcaneWorkbench extends Block {

  public ArcaneWorkbench() {
    super(
      AbstractBlock.Properties.create(Material.WOOD)
        .hardnessAndResistance(3, 10)
        .harvestTool(ToolType.AXE)
        .sound(SoundType.WOOD)
        .notSolid()
    );
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return ModTileEntityTypes.ARCANE_WORKBENCH.get().create();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      final TileEntity tileEntity = world.getTileEntity(pos);
      if (tileEntity instanceof ArcaneWorkbenchTE)
        NetworkHooks.openGui((ServerPlayerEntity)player, (ArcaneWorkbenchTE)tileEntity, pos);
    }

    return ActionResultType.SUCCESS;
  }
}
