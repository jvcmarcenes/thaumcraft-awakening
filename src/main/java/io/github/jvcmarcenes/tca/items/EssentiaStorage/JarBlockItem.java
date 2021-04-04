package io.github.jvcmarcenes.tca.items.EssentiaStorage;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.blocks.EssentiaJar.EssentiaJarTE;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JarBlockItem extends BlockItem implements IAspectStorage {

  public JarBlockItem(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  protected boolean onBlockPlaced(BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState state) {
    boolean ret = super.onBlockPlaced(pos, world, player, stack, state);

    EssentiaJarTE te = (EssentiaJarTE)world.getTileEntity(pos);

    te.fill(IAspectStorage.getStoredAspect(stack), getStoredAmount(stack));

    return ret;
  }

  @Override
  public int getStoredAmount(ItemStack stack) { return stack.hasTag() ? stack.getTag().getInt("amount") : 0; }

  @Override
  public int getStorageLeft(ItemStack stack) { return stack.hasTag() ? EssentiaJarTE.MAX_STORAGE - stack.getTag().getInt("amount") : EssentiaJarTE.MAX_STORAGE; }

  @Override
  public boolean fillsPartially() { return true; }

  @Override
  public ItemStack create(String aspect, int amount, int count) {
    ItemStack stack = new ItemStack(ModItems.ESSENTIA_JAR.get(), count);

    if (aspect.equals(Aspects.NONE) || amount == 0) return stack;

    IAspectStorage.setStoredAspect(stack, aspect);
    stack.getOrCreateTag().putInt("amount", amount);

    return stack;
  }
}
