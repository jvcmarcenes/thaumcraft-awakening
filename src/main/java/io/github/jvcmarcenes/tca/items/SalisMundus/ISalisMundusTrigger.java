package io.github.jvcmarcenes.tca.items.SalisMundus;

import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public interface ISalisMundusTrigger {

    HashSet<ISalisMundusTrigger> triggers = new HashSet<>();

    static void addTrigger(ISalisMundusTrigger trigger) {
        triggers.add(trigger);
    }

    static void registerTriggers() {
        addTrigger(new BlockSalisMundusTrigger(Blocks.CRAFTING_TABLE, ModBlocks.ARCANE_WORKBENCH.get()));
        addTrigger(new BlockSalisMundusTrigger(Blocks.CAULDRON, ModBlocks.CRUCIBLE.get()));
        addTrigger(new BlockToItemSalisTrigger(Blocks.BOOKSHELF, new ItemStack(Items.BOOK, 3)));

        ItemStack stack = new ItemStack(ModItems.CREATIVE_CELL.get());
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("visCap", 1000);
        nbt.putInt("currentVis", 1000);
        stack.setTag(nbt);
        addTrigger(new BlockToItemSalisTrigger(Blocks.DIAMOND_BLOCK, stack));
    }

    boolean validate(World world, PlayerEntity player, BlockPos pos);
    void execute(World world, PlayerEntity player, BlockPos pos, ItemUseContext context);
}
