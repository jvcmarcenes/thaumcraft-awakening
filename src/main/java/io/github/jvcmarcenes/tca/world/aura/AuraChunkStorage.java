package io.github.jvcmarcenes.tca.world.aura;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

public class AuraChunkStorage implements Capability.IStorage<AuraChunk> {

  private static final String MAX_TAG = "max";
  private static final String VIS_TAG = "vis";
  private static final String FLUX_TAG = "flux";

  @Override
  public INBT writeNBT(Capability<AuraChunk> cap, AuraChunk ac, Direction side) {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(MAX_TAG, ac.getMax());
    nbt.putInt(VIS_TAG, ac.getVis());
    nbt.putInt(FLUX_TAG, ac.getFlux());
    return nbt;
  }

  @Override
  public void readNBT(Capability<AuraChunk> cap, AuraChunk ac, Direction side, INBT tag) {
    CompoundNBT nbt = (CompoundNBT)tag;
    ac.setMax(nbt.getInt(MAX_TAG));
    ac.setVis(nbt.getInt(VIS_TAG));
    ac.setFlux(nbt.getInt(FLUX_TAG));
  }

  public static class Factory implements Callable<AuraChunk> {
    @Override
    public AuraChunk call() {
      return new AuraChunk();
    }
  }
}
