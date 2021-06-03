package io.github.jvcmarcenes.tca.world.aura;

import io.github.jvcmarcenes.tca.init.ModCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

@SuppressWarnings("unchecked")
public class AuraChunkCapabilityProvider implements ICapabilitySerializable<INBT> {

  private final float auraAffinity;
  private final boolean tainted;

  public AuraChunkCapabilityProvider(float auraAffinity, boolean tainted) {
    this.auraAffinity = auraAffinity;
    this.tainted = tainted;
  }

  private AuraChunk auraChunk;

  private AuraChunk getCachedAuraChunk() {
    if (auraChunk == null) auraChunk = new AuraChunk(auraAffinity, tainted);
    return auraChunk;
  }

  private LazyOptional<AuraChunk> lazyAura = LazyOptional.of(this::getCachedAuraChunk);

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap == ModCapabilities.AURA_CHUNK_CAP
      ? (LazyOptional<T>) lazyAura
      : LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    return ModCapabilities.AURA_CHUNK_CAP.writeNBT(getCachedAuraChunk(), null);
  }

  @Override
  public void deserializeNBT(INBT nbt) {
    ModCapabilities.AURA_CHUNK_CAP.readNBT(getCachedAuraChunk(), null, nbt);
  }
}
