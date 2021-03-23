package io.github.jvcmarcenes.tca.items.SalisMundus;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockToItemSalisTrigger extends BlockSalisMundusTrigger {

    private final ItemStack result;

    public BlockToItemSalisTrigger(Block trigger, ItemStack result) {
        super(trigger, Blocks.AIR);
        this.result = result;
    }

    public BlockToItemSalisTrigger(Block trigger, Block resultBlock, ItemStack resultStack) {
        super(trigger, resultBlock);
        this.result = resultStack;
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos pos, ItemUseContext context) {
        super.execute(world, player, pos, context);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result);
        world.addEntity(itemEntity);
    }
}
