package io.github.jvcmarcenes.tca.blocks.ResearchTable;

import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.ItemStackHandler;

public class ResearchTable extends HorizontalBlock {

  public ResearchTable() {
    super(
      AbstractBlock.Properties.create(Material.WOOD)
        .hardnessAndResistance(1.0f)
        .harvestTool(ToolType.AXE)
        .notSolid()
    );

    setDefaultState(stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(PART, Part.LEFT));
  }

  public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

  @Override
  protected void fillStateContainer(Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);

    builder.add(HORIZONTAL_FACING);
    builder.add(PART);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return state.get(PART) == Part.RIGHT;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return ModTileEntityTypes.RESEARCH_TABLE.get().create();
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    
    BlockPos neighbourPos = getNeighbourPos(pos, state);
    BlockState neighbour = world.getBlockState(neighbourPos);

    ItemStackHandler inv = getTileEntity(world, pos, state).inventory;
    for (int i = 0; i < inv.getSlots(); i++)
      InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i));

    world.setBlockState(neighbourPos, Blocks.AIR.getDefaultState(), 35);
    world.playEvent(player, 2001, neighbourPos, Block.getStateId(neighbour));

    super.onBlockHarvested(world, pos, state, player);
  }

  private BlockPos getNeighbourPos(BlockPos pos, BlockState state) {
    Direction forward = state.get(HORIZONTAL_FACING);
    return pos.offset(state.get(PART) == Part.LEFT ? forward.rotateY() : forward.rotateYCCW());
  }

  private ResearchTableTE getTileEntity(World world, BlockPos pos, BlockState state) {
    BlockPos tePos = state.get(PART) == Part.RIGHT ? pos : getNeighbourPos(pos, state);
    return (ResearchTableTE)world.getTileEntity(tePos);
  }

  public static enum Part implements IStringSerializable { 
    LEFT("left"), RIGHT("right");

    private final String name;

    private Part(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Override
    public String getString() {
      return this.name;
    }
  }
}
