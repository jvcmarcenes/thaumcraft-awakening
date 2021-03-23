package io.github.jvcmarcenes.tca.items.SalisMundus;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSalisMundusTrigger implements ISalisMundusTrigger{

    private final Block trigger;
    private final BiFunction<BlockState, BlockItemUseContext, BlockState> result;

    public BlockSalisMundusTrigger(Block trigger, Block result) {
        this(trigger, (state, context) -> result.getStateForPlacement(context));
    }

    public BlockSalisMundusTrigger(Block trigger, BiFunction<BlockState, BlockItemUseContext, BlockState> result) {
        this.trigger = trigger;
        this.result = result;
    }

    public boolean validate(World world, PlayerEntity player, BlockPos pos) {
        return world.getBlockState(pos).getBlock().equals(trigger);
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos pos, ItemUseContext context) {
        BlockItemUseContext blockcontext = new BlockItemUseContext(context);
        world.setBlockState(pos, result.apply(world.getBlockState(pos), blockcontext));
    }
}
