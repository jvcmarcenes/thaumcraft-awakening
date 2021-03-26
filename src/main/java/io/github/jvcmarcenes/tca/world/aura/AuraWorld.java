package io.github.jvcmarcenes.tca.world.aura;

import io.github.jvcmarcenes.tca.init.ModCapabilities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class AuraWorld {

  public static AuraChunk getAuraChunkAt(World world, ChunkPos pos) {
    return getAuraChunkAt(world, pos.asBlockPos());
  }

  public static AuraChunk getAuraChunkAt(World world, BlockPos pos) {
    return getAuraForChunk(world.getChunkAt(pos));
  }

  public static AuraChunk getAuraForChunk(Chunk chunk) {
    return chunk.getCapability(ModCapabilities.AURA_CHUNK_CAP)
    .orElseThrow(() -> new IllegalStateException("Illegal chunk without aura."));
  }
}
