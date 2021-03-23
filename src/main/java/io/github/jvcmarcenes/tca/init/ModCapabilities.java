package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.world.aura.AuraChunk;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    @CapabilityInject(AuraChunk.class)
    public static final Capability<AuraChunk> AURA_CHUNK_CAP = null;
    public static final ResourceLocation AURA_CHUNK_ID = new ResourceLocation(TCA.MOD_ID, "aura_chunk");

}
