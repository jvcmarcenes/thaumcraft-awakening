package io.github.jvcmarcenes.tca.blocks.Crucible;

import java.util.Random;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.alchemy.IAspectStorage;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class Crucible extends Block {

  public Crucible() {
    super(
      AbstractBlock.Properties.create(Material.IRON)
        .hardnessAndResistance(3, 10)
        .harvestTool(ToolType.PICKAXE)
        .sound(SoundType.METAL)
    );

    this.setDefaultState(
      this.stateContainer.getBaseState()
        .with(LEVEL, 0)
        .with(BOILING, false)
    );
  }

  public static final int MAX_LEVEL = 15;
  public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, MAX_LEVEL);
  public static final BooleanProperty BOILING = BooleanProperty.create("boiling");

  @Override
  protected void fillStateContainer(Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(LEVEL);
    builder.add(BOILING);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return ModTileEntityTypes.CRUCIBLE.get().create();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (world.isRemote) return ActionResultType.SUCCESS;

    CrucibleTE te = (CrucibleTE)world.getTileEntity(pos);
    ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

    //TODO add sfx 
    if (Items.WATER_BUCKET.equals(stack.getItem()) && state.get(LEVEL) < MAX_LEVEL) {
      world.setBlockState(pos, state.with(LEVEL, MAX_LEVEL));

      stack.shrink(1);
      player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
    } else if (state.get(LEVEL) > 0){
      if (stack.getItem() instanceof IAspectStorage && IAspectStorage.getStoredAspect(stack) == Aspects.NONE) {
        int amount = ((IAspectStorage)stack.getItem()).getStoredAmount();
        String aspect = te.aspects.getRandomAspectMin(amount);

        te.aspects.drain(aspect, amount);
        world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) - 1));

        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        IAspectStorage.setStoredAspect(newStack, aspect);
        
        stack.shrink(1);
        player.inventory.addItemStackToInventory(newStack);
      } else if (state.get(BOILING)) {
        te.meltItem(player.getHeldItem(Hand.MAIN_HAND));
        stack.shrink(1);
      }
    }

    return ActionResultType.SUCCESS;
  }

  @Override
  public void fillWithRain(World world, BlockPos pos) {
    if (world.rand.nextInt(20) > 1) return;
    if (world.getBiome(pos).getTemperature(pos) < 0.15f) return;

    BlockState state = world.getBlockState(pos);
    if (state.get(LEVEL) < MAX_LEVEL) world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) + 1));
  }

  @Override
  public boolean ticksRandomly(BlockState state) {
    return state.get(LEVEL) > 0;
  }

  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    CrucibleTE te = (CrucibleTE)world.getTileEntity(pos);
    String aspect = te.aspects.getRandomAspect();
    te.aspects.drain(aspect, 1);
  }

  @Override // TODO fix particle spawning
  public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
    if (!state.get(BOILING)) return;

    float x = pos.getX() + (rand.nextInt(11) + 2)/11;
    float y = (float)pos.getY() + (((float)state.get(LEVEL)/(float)MAX_LEVEL) * 11 + 4)/16f;
    float z = pos.getZ() + (rand.nextInt(11) + 2)/11;

    world.addParticle(ParticleTypes.BUBBLE, false, x, y, z, 0f, .1f, 0f);
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (state.get(LEVEL) > 0 && state.get(BOILING))
      entity.attackEntityFrom(DamageSource.IN_FIRE, 1f);
  }

  private static final VoxelShape INSIDE = makeCuboidShape(2, 4, 2, 14, 16, 14);
  private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0, 0, 3, 16, 3, 13), makeCuboidShape(3, 0, 0, 13, 3, 16), makeCuboidShape(2, 0, 2, 14, 3, 14), INSIDE), IBooleanFunction.ONLY_FIRST);

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) { return SHAPE; }

  @Override
  public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) { return INSIDE; }
}
