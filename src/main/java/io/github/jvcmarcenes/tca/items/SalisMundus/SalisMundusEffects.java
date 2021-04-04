package io.github.jvcmarcenes.tca.items.SalisMundus;

import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.jvcmarcenes.tca.blocks.Crucible.Crucible;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
    addEffect(Blocks.CRAFTING_TABLE, context -> { //Crafting Table --> Arcane Workbench
      context.getWorld().setBlockState(context.getPos(), ModBlocks.ARCANE_WORKBENCH.get().getDefaultState());
    });

    addEffect(Blocks.CAULDRON, context -> { // Cauldron --> Crucible
      int cauldronLevel = context.getWorld().getBlockState(context.getPos()).get(CauldronBlock.LEVEL);
      int crucibleLevel = (int)Math.floor(((float)cauldronLevel * (float)Crucible.MAX_LEVEL/3f));
      context.getWorld().setBlockState(context.getPos(), ModBlocks.CRUCIBLE.get().getDefaultState().with(Crucible.LEVEL, crucibleLevel));
    });

    addEffect(Blocks.BOOKSHELF, context -> { // Bookshelf --> Thaumonomicon
      World world =  context.getWorld();
      BlockPos pos = context.getPos();
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemStack stack = new ItemStack(ModItems.THAUMONOMICON.get());
      // ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
      // world.addEntity(itemEntity);
      spawnItemStack(world, pos, stack);
    });

    addEffect(Blocks.DIAMOND_BLOCK, context -> { // DEV_ONLY Diamond Block --> Creative Cell
      World world = context.getWorld();
      BlockPos pos = context.getPos();
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemStack stack = new ItemStack(ModItems.CREATIVE_CELL.get());
      CompoundNBT tag = new CompoundNBT();
      tag.putInt("visCap", 1000);
      tag.putInt("currentVis", 1000);
      stack.setTag(tag);
      // ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
      // world.addEntity(itemEntity);
      spawnItemStack(world, pos, stack);
    });
  }

  private static void spawnItemStack(World world, BlockPos pos, ItemStack stack) {
    InventoryHelper.spawnItemStack(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack);
  }
}
