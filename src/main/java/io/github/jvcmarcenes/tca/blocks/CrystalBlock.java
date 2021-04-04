package io.github.jvcmarcenes.tca.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class CrystalBlock extends Block {

  public CrystalBlock(Properties builder) {
    super(builder);

    this.setDefaultState(
      this.stateContainer.getBaseState()
        .with(FACE, Direction.DOWN)
        .with(GROWTH, 0)
    );
  }

  public static final DirectionProperty FACE = DirectionProperty.create("face", Direction.values());
  public static final IntegerProperty GROWTH = IntegerProperty.create("growth", 0, 3);

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);

    builder.add(FACE);
    builder.add(GROWTH);
  }
}
