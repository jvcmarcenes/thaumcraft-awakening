package io.github.jvcmarcenes.tca.items;

import java.util.Optional;

import io.github.jvcmarcenes.tca.blocks.ResearchTable.ResearchTable;
import io.github.jvcmarcenes.tca.blocks.ResearchTable.ResearchTableTE;
import io.github.jvcmarcenes.tca.blocks.ResearchTable.ResearchTable.Part;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ScribingTools extends Item {

  public ScribingTools(Properties properties) {
    super(properties);
  }

  private Optional<Direction> getNeighbouringTable(World world, BlockPos pos) {
    if (world.getBlockState(pos.west()).isIn(ModBlocks.TABLE.get())) return Optional.of(Direction.WEST);
    else if (world.getBlockState(pos.north()).isIn(ModBlocks.TABLE.get())) return Optional.of(Direction.NORTH);
    else if (world.getBlockState(pos.east()).isIn(ModBlocks.TABLE.get())) return Optional.of(Direction.EAST);
    else if (world.getBlockState(pos.south()).isIn(ModBlocks.TABLE.get())) return Optional.of(Direction.SOUTH);
    else return Optional.empty();
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    
    World world = context.getWorld();
    BlockPos pos = context.getPos();

    if (!world.getBlockState(pos).isIn(ModBlocks.TABLE.get())) return ActionResultType.PASS;

    Optional<Direction> neighbour = getNeighbouringTable(world, pos);
    if (!neighbour.isPresent()) return ActionResultType.PASS;

    Direction dir = neighbour.get();
    Direction facing = dir.rotateYCCW();

    world.setBlockState(pos, ModBlocks.RESEARCH_TABLE.get().getDefaultState().with(ResearchTable.HORIZONTAL_FACING, facing).with(ResearchTable.PART, Part.LEFT));
    world.setBlockState(pos.offset(dir), ModBlocks.RESEARCH_TABLE.get().getDefaultState().with(ResearchTable.HORIZONTAL_FACING, facing).with(ResearchTable.PART, Part.RIGHT));

    ResearchTableTE te = (ResearchTableTE)world.getTileEntity(pos.offset(dir));
    te.inventory.setStackInSlot(ResearchTableTE.SCRIBING_SLOT, context.getItem().copy());

    context.getItem().shrink(1);

    return ActionResultType.SUCCESS;
  }
  
}
