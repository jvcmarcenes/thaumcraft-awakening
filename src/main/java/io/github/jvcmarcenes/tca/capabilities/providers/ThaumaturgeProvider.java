package io.github.jvcmarcenes.tca.capabilities.providers;

import io.github.jvcmarcenes.tca.capabilities.PlayerWarp;
import io.github.jvcmarcenes.tca.init.ModCapabilities;
import io.github.jvcmarcenes.tca.research.knowledge.PlayerKnowledge;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

@SuppressWarnings("unchecked")
public class ThaumaturgeProvider implements ICapabilitySerializable<INBT> {

  private PlayerKnowledge knowledge;
  private PlayerWarp warp;

  private PlayerKnowledge getKnowledge() {
    if (knowledge == null) knowledge = new PlayerKnowledge();
    return knowledge;
  }

  private PlayerWarp getWarp() {
    if (warp == null) warp = new PlayerWarp();
    return warp;
  }

  private LazyOptional<PlayerKnowledge> lazyKnowledge = LazyOptional.of(this::getKnowledge);
  private LazyOptional<PlayerWarp> lazyWarp = LazyOptional.of(this::getWarp);

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {

    if (cap == ModCapabilities.PLAYER_KNOWLEDGE_CAP) 
      return (LazyOptional<T>) lazyKnowledge;

    if (cap == ModCapabilities.PLAYER_WARP_CAP)
      return (LazyOptional<T>) lazyWarp;

    return LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(INBT nbt) {
    
  }
}
