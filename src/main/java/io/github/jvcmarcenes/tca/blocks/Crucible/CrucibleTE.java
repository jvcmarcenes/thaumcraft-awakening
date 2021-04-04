package io.github.jvcmarcenes.tca.blocks.Crucible;

import java.util.Optional;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipeInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CrucibleTE extends TileEntity implements ITickableTileEntity {

  public final AspectGroup aspects = new AspectGroup();
  private int heat = 0;
  private final int boilingHeat = 400;
  private final int heatDamper = 40;

  public CrucibleTE() { super(ModTileEntityTypes.CRUCIBLE.get()); }
  
  @Override
  public void tick() {
    updateBoilingState();
  }

  private boolean isHeatSource(BlockState state) {
    return state.isInAndMatches(BlockTags.CAMPFIRES, campfire -> campfire.get(CampfireBlock.LIT))
      || state.isIn(Blocks.FIRE);
  }

  private void updateBoilingState() {
    if (getBlockState().get(Crucible.LEVEL) == 0) {
      if (getBlockState().get(Crucible.BOILING))
        world.setBlockState(getPos(), getBlockState().with(Crucible.BOILING, false));

      return;
    }

    boolean hasHeatSource = isHeatSource(world.getBlockState(getPos().down()));
    boolean isBoiling = getBlockState().get(Crucible.BOILING);

    if (hasHeatSource && heat < boilingHeat) heat++;
    else if (!hasHeatSource && heat > 0) heat--;

    if (!isBoiling && heat >= boilingHeat - heatDamper)
      world.setBlockState(getPos(), getBlockState().with(Crucible.BOILING, true));
    else if (isBoiling && heat <= heatDamper)
      world.setBlockState(getPos(), getBlockState().with(Crucible.BOILING, false));
  }

  public void meltItemStack(ItemStack in) {
    if (in.isEmpty()) return;

    for (int i = 0; i < in.getCount(); i++) {
      boolean wasCatalyst = tryCraft(in);
      if (!wasCatalyst) aspects.addAll(Aspects.get(in));
    }

    world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
  }

  // tries to craft a recipe, returns true if there was a recipe crafted
  private boolean tryCraft(ItemStack in) {
    AlchemyRecipeInventory inv = new AlchemyRecipeInventory(aspects, in.getItem());

    assert world != null;
    Optional<AlchemyRecipe> recipe = world.getRecipeManager().getRecipe(ModRecipeTypes.ALCHEMY, inv, world);
    if (!recipe.isPresent()) return false;

    aspects.drainAll(recipe.get().getRequiredAspects());
    world.setBlockState(pos, getBlockState().with(Crucible.LEVEL, getBlockState().get(Crucible.LEVEL) - 1));
    //TODO play sound

    ItemEntity itemEntity = new ItemEntity(world, getPos().getX(), getPos().getY() + 1.2, getPos().getZ(), recipe.get().getCraftingResult(inv));
    itemEntity.setNoGravity(true);
    world.addEntity(itemEntity);

    return true;
  }

  // NBT (de)serialization
  private static final String ASPECTS_TAG = "aspects";
  private static final String HEAT_TAG = "heat";

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);

    aspects.deserializeNBT(nbt.getCompound(ASPECTS_TAG));
    heat = nbt.getInt(HEAT_TAG);

  }

  @Override
  public CompoundNBT write(CompoundNBT nbt) {
    super.write(nbt);

    nbt.put(ASPECTS_TAG, aspects.serializeNBT());
    nbt.putInt(HEAT_TAG, heat);

    return nbt;
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  // Client-Server Sync
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    CompoundNBT tag = aspects.serializeNBT();

    return new SUpdateTileEntityPacket(getPos(), -1, tag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    CompoundNBT tag = pkt.getNbtCompound();

    aspects.deserializeNBT(tag);

    //super.onDataPacket(net, pkt);
  }
}
