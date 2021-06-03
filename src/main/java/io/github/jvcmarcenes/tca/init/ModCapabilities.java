package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.capabilities.PlayerWarp;
import io.github.jvcmarcenes.tca.research.knowledge.PlayerKnowledge;
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
	
	@CapabilityInject(PlayerKnowledge.class)
	public static final Capability<PlayerKnowledge> PLAYER_KNOWLEDGE_CAP = null;
	public static final ResourceLocation PLAYER_KNOWLEDGE_ID = new ResourceLocation(TCA.MOD_ID, "player_knowledge");
	
	@CapabilityInject(PlayerWarp.class)
	public static final Capability<PlayerWarp> PLAYER_WARP_CAP = null;
	public static final ResourceLocation PLAYER_WARP_ID = new ResourceLocation(TCA.MOD_ID, "player_warp");
	
}
