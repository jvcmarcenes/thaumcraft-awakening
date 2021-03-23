package io.github.jvcmarcenes.tca.world.aura;

import com.google.common.collect.Iterables;
import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.init.ModCapabilities;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;

import java.util.*;
import java.util.stream.Collectors;

public class AuraTickHandler {

    public static void auraTick(World world) {
        ServerChunkProvider serverChunkProvider = (ServerChunkProvider) world.getChunkProvider();

        Iterable<ChunkHolder> iterable = TCA.getLoadedChunksIterable(serverChunkProvider.chunkManager);
        if (iterable == null) return;

        List<ChunkHolder> chunkHolders =
            Arrays.stream(Iterables.toArray(iterable, ChunkHolder.class)).collect(Collectors.toList());

        int length = chunkHolders.size();
        int n = 80;
        if (length < n) n = length;

        for (int i = length - 1; i >= length - n; i-- ) {
            Collections.swap(chunkHolders, i, TCA.RANDOM.nextInt(i + 1));
        }

        List<ChunkHolder> toIterate = chunkHolders.subList(length - n, length);

        for(ChunkHolder chunkHolder : toIterate) {
            Chunk chunk = chunkHolder.getChunkIfComplete();
            if (chunk == null) continue;

            AuraChunk auraChunk = AuraWorld.getAuraForChunk(chunk);
            if (auraChunk == null) {
                TCA.LOGGER.error("No Aura Attached to chunk at " + chunk.getPos());
                continue;
            }

            auraChunk.tick();
        }
    }
}
