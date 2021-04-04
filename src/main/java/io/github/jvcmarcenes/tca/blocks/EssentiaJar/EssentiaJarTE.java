package io.github.jvcmarcenes.tca.blocks.EssentiaJar;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.client.models.JarBakedModel;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

public class EssentiaJarTE extends TileEntity {
  
  public EssentiaJarTE() { super(ModTileEntityTypes.JAR.get()); }

  public static final int MAX_STORAGE = 160;

  private String aspect = Aspects.NONE;
  private int amount = 0;

  public int getAmount() {
    return amount;
  }

  public String getAspect() {
    return aspect;
  }

  private void updateLightLevel() {
    if (aspect.equals(Aspects.NONE)) {
      world.setBlockState(getPos(), getBlockState().with(EssentiaJar.LIGHT, 0));
    } else if (aspect.equals(Aspects.LUX)) {
      world.setBlockState(getPos(), getBlockState().with(EssentiaJar.LIGHT, amount <= MAX_STORAGE/2 ? 2 : 3));
    } else {
      world.setBlockState(getPos(), getBlockState().with(EssentiaJar.LIGHT, 1));
    }
  }

  public int fill(String fillAspect, int fillAmount) {
    if (!aspect.equals(Aspects.NONE) && !fillAspect.equals(aspect)) return 0;

    if (fillAmount > MAX_STORAGE - amount) fillAmount = MAX_STORAGE - amount;

    amount += fillAmount;
    aspect = fillAspect;

    updateLightLevel();
    world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
    return fillAmount;
  }

  public int drain(int drainAmount) {
    if (aspect.equals(Aspects.NONE)) return 0;

    if (drainAmount > amount) drainAmount = amount;

    amount -= drainAmount;

    if (amount == 0) aspect = Aspects.NONE;

    updateLightLevel();
    world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
    return drainAmount;
  }

  // Serialization
  private static final String ASPECT_TAG = "aspect";
  private static final String AMOUNT_TAG = "amount";

  @Override
  public void read(BlockState state, CompoundNBT tag) {
    super.read(state, tag);

    aspect = tag.getString(ASPECT_TAG);
    amount = tag.getInt(AMOUNT_TAG);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    super.write(tag);

    tag.putString(ASPECT_TAG, aspect);
    tag.putInt(AMOUNT_TAG, amount);

    return tag;
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  // Client-Server Sync
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    CompoundNBT tag = new CompoundNBT();

    tag.putString(ASPECT_TAG, aspect);
    tag.putInt(AMOUNT_TAG, amount);

    return new SUpdateTileEntityPacket(getPos(), -1, tag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    CompoundNBT tag = pkt.getNbtCompound();

    aspect = tag.getString(ASPECT_TAG);
    amount = tag.getInt(AMOUNT_TAG);

    requestModelDataUpdate();
    world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
  }

  @Override
  public IModelData getModelData() {
    ModelDataMap modelData = new ModelDataMap.Builder().withInitial(JarBakedModel.LEVEL, amount).build();
    return modelData;
  }

}
