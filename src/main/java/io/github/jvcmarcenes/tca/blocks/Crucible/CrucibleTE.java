package io.github.jvcmarcenes.tca.blocks.Crucible;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipeInventory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class CrucibleTE extends TileEntity implements ITickableTileEntity {

  public final AspectGroup aspects = new AspectGroup();
  private int heat = 0;

  public CrucibleTE() { super(ModTileEntityTypes.CRUCIBLE.get()); }
  
  @Override
  public void tick() {
    if (getBlockState().get(Crucible.LEVEL) == 0) return;

    updateBoilingState();
    if (!getBlockState().get(Crucible.BOILING)) return;

    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    AxisAlignedBB aabb = new AxisAlignedBB(x + 2/16, y + 3/16, z + 2/16, x + 14/16, y + 17/16, z + 14/16);
    List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, aabb, entity -> !Aspects.get(entity.getItem()).hasNone());
    items.forEach(item -> {
      meltItem(item.getItem());
      item.remove();
    });
  }

  private void updateBoilingState() {
    boolean hasHeatSource = world.getBlockState(getPos().down()).isIn(Blocks.TORCH);
    boolean isBoiling = getBlockState().get(Crucible.BOILING);

    if (hasHeatSource && heat < 400) heat++;
    else if (!hasHeatSource && heat > 0) heat--;

    if (!isBoiling && heat >= 360)
      world.setBlockState(getPos(), world.getBlockState(pos).with(Crucible.BOILING, true));
    else if (isBoiling && heat <= 40)
      world.setBlockState(getPos(), world.getBlockState(pos).with(Crucible.BOILING, false));
  }

  public void meltItem(ItemStack in) {
    if (in.isEmpty()) return;

    boolean wasCatalyst = tryCraft(in);
    if (!wasCatalyst) aspects.addAll(Aspects.get(in));

    world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
  }

  private boolean tryCraft(ItemStack in) {
    AlchemyRecipeInventory inv = new AlchemyRecipeInventory(aspects, in.getItem());

    assert world != null;
    Optional<AlchemyRecipe> recipe = world.getRecipeManager().getRecipe(ModRecipeTypes.ALCHEMY, inv, world);
    if (!recipe.isPresent()) return false;

    aspects.drainAll(recipe.get().getRequiredAspects());
    world.setBlockState(pos, getBlockState().with(Crucible.LEVEL, getBlockState().get(Crucible.LEVEL) - 1));
    //TODO play sound

    world.addEntity(new ItemEntity(world, getPos().getX(), getPos().getY() + 1, getPos().getZ(), recipe.get().getCraftingResult(inv)));

    return true;
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    CompoundNBT tag = new CompoundNBT();
    tag.putInt("size", aspects.size());

    AtomicInteger i = new AtomicInteger(0);
    aspects.forEach((aspect, amount) -> {
      tag.putString("aspect" + i, aspect);
      tag.putInt("amount" + i, amount);
      i.incrementAndGet();
    });

    return new SUpdateTileEntityPacket(getPos(), -1, tag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    CompoundNBT tag = pkt.getNbtCompound();
    int size = tag.getInt("size");

    aspects.clear();

    for (int i = 0; i < size; i++) {
      String aspect = tag.getString("aspect" + i);
      int amount = tag.getInt("amount" + i);

      aspects.add(aspect, amount);
    }

    super.onDataPacket(net, pkt);
  }
}
