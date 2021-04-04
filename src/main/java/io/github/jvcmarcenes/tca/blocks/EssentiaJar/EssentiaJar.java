package io.github.jvcmarcenes.tca.blocks.EssentiaJar;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.IAspectStorage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class EssentiaJar extends Block {

  public EssentiaJar() {
    super(
      AbstractBlock.Properties.create(Material.GLASS)
        .zeroHardnessAndResistance()
        .setLightLevel(state -> {
          switch (state.get(LIGHT)) {
            case 0: return 0;
            case 1: return 1;
            case 2: return 7;
            case 3: return 14;
            default: return 0;
          }
        })
        .notSolid()
    );

    this.setDefaultState(
      this.stateContainer.getBaseState()
        .with(LIGHT, 0)
    );
  }

  public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 3);

  @Override
  protected void fillStateContainer(Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);

    builder.add(LIGHT);
  }

  @Override
  public boolean hasTileEntity(BlockState state) { return true; }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) { return ModTileEntityTypes.JAR.get().create(); }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (world.isRemote) return ActionResultType.SUCCESS;

    EssentiaJarTE te = (EssentiaJarTE)world.getTileEntity(pos);
    ItemStack stack = player.getHeldItem(hand);

    if (!(stack.getItem() instanceof IAspectStorage)) return ActionResultType.PASS;

    IAspectStorage aspectStorage = (IAspectStorage)stack.getItem();
      
    String aspect = IAspectStorage.getStoredAspect(stack);
    int storageLeft = aspectStorage.getStorageLeft(stack);
    int storedAmount = aspectStorage.getStoredAmount(stack);

    if ((storedAmount == 0 && te.getAmount() > 0) || (storageLeft > 0 && te.getAspect().equals(aspect))) {
    // Drain the essentia from the Jar

      if (!aspectStorage.fillsPartially() && storageLeft > te.getAmount()) return ActionResultType.PASS;

      String drainedAspect = te.getAspect();

      int drainAmount = Math.min(storageLeft, te.getAmount());
      int drainedAmount = te.drain(drainAmount);

      ItemStack newStack = new ItemStack(stack.getItem());
      IAspectStorage.setStoredAspect(newStack, drainedAspect);
      newStack.getOrCreateTag().putInt("amount", storedAmount + drainedAmount);

      stack.shrink(1);
      player.inventory.addItemStackToInventory(aspectStorage.create(drainedAspect, storedAmount + drainedAmount, 1));

      return ActionResultType.SUCCESS;

    } else if (storedAmount > 0 && (te.getAspect().equals(Aspects.NONE) || (te.getAspect().equals(aspect) && te.getAmount() < EssentiaJarTE.MAX_STORAGE))) {
    // Fill the Jar with the Phial's essentia

      if (!aspectStorage.fillsPartially() && storedAmount > EssentiaJarTE.MAX_STORAGE - storedAmount) return ActionResultType.PASS;

      int fillAmount = Math.min(storedAmount, EssentiaJarTE.MAX_STORAGE - te.getAmount());
      int filledAmount = te.fill(aspect, fillAmount);

      stack.shrink(1);
      player.inventory.addItemStackToInventory(aspectStorage.create(aspect, storedAmount - filledAmount, 1));

      return ActionResultType.SUCCESS;
    }

    return ActionResultType.PASS;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.combineAndSimplify(makeCuboidShape(3, 0, 3, 13, 12, 13), makeCuboidShape(5, 12, 5, 11, 14, 11), IBooleanFunction.OR);
  }
}
