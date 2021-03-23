package io.github.jvcmarcenes.tca.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.StateContainer;

public class CrystalBlock extends HorizontalBlock {

    public CrystalBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);

        builder.add(HORIZONTAL_FACING);
    }
}
