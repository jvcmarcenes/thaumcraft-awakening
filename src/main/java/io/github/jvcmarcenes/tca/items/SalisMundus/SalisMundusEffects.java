package io.github.jvcmarcenes.tca.items.SalisMundus;

import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SalisMundusEffects {
  
  private static final Hashtable<Block, Consumer<ItemUseContext>> effects = new Hashtable<>();

  public static Optional<Consumer<ItemUseContext>> getEffect(Block key) {
    return effects.containsKey(key) ? Optional.of(effects.get(key)) : Optional.empty();
  }

  public static void addEffect(Block key, Consumer<ItemUseContext> effect) {
    effects.put(key, effect);
  }

  public static void registerEffects() {
    addEffect(Blocks.CRAFTING_TABLE, context -> {
      context.getWorld().setBlockState(context.getPos(), ModBlocks.ARCANE_WORKBENCH.get().getDefaultState());
    });

    addEffect(Blocks.CAULDRON, context -> {
      context.getWorld().setBlockState(context.getPos(), ModBlocks.CRUCIBLE.get().getDefaultState());
    });

    addEffect(Blocks.BOOKSHELF, context -> {
      World world =  context.getWorld();
      BlockPos pos = context.getPos();
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemStack stack = new ItemStack(Items.BOOK, 3);
      ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
      world.addEntity(itemEntity);
    });

    addEffect(Blocks.DIAMOND_BLOCK, context -> {
      World world = context.getWorld();
      BlockPos pos = context.getPos();
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemStack stack = new ItemStack(ModItems.CREATIVE_CELL.get());
      CompoundNBT tag = new CompoundNBT();
      tag.putInt("visCap", 1000);
      tag.putInt("currentVis", 1000);
      stack.setTag(tag);
      ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
      world.addEntity(itemEntity);
    });
  }
}
