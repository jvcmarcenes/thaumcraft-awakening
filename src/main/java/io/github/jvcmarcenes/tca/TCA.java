package io.github.jvcmarcenes.tca;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.*;
import io.github.jvcmarcenes.tca.world.aura.AuraTickHandler;
import io.github.jvcmarcenes.tca.world.aura.BiomeAuraAffinity;
import io.github.jvcmarcenes.tca.client.ClientEventHandler;
import io.github.jvcmarcenes.tca.client.ClientEventHandlerForge;
import io.github.jvcmarcenes.tca.items.SalisMundus.ISalisMundusTrigger;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import io.github.jvcmarcenes.tca.world.aura.AuraChunkCapabilityProvider;
import io.github.jvcmarcenes.tca.world.aura.AuraChunk;
import io.github.jvcmarcenes.tca.world.aura.AuraChunkStorage;
import io.github.jvcmarcenes.tca.world.biomes.ModBiomes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Random;

@Mod(TCA.MOD_ID)
public class TCA {
    public static final String MOD_ID = "tca";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Random RANDOM = new Random();

    public TCA() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBiomes.BIOMES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(ModCapabilities.class);
        MinecraftForge.EVENT_BUS.register(ModRecipeTypes.class);

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ClientEventHandlerForge.class);
    }

    public void setup(final FMLCommonSetupEvent event) {
        setupReflection();

        //Register Capabilities
        CapabilityManager.INSTANCE.register(AuraChunk.class, new AuraChunkStorage(), new AuraChunkStorage.Factory());
        //

        BiomeAuraAffinity.registerBiomeAuraAffinities();
        Aspects.registerItemAspects();
        Aspects.registerEntityAspects();
        ISalisMundusTrigger.registerTriggers();
    }

    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event) {
        if (event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END) return;

        AuraTickHandler.auraTick(event.world);
    }

    @SubscribeEvent
    public void attachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> event) {
        Chunk chunk = event.getObject();

        /*
        World world = chunk.getWorld();
        ChunkPos pos = chunk.getPos();

        Chunk chunkNorth = world.getChunk(pos.x, pos.z + 1);
        Chunk chunkSouth = world.getChunk(pos.x, pos.z - 1);
        Chunk chunkEast = world.getChunk(pos.x + 1, pos.z);
        Chunk chunkWest = world.getChunk(pos.x - 1, pos.z);
        */

        float auraAffinity = BiomeAuraAffinity.getAuraAffinityForChunk(chunk);
        event.addCapability(
            ModCapabilities.AURA_CHUNK_ID,
            new AuraChunkCapabilityProvider(auraAffinity, false)
        );
    }

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.GOLDEN_AXE);
        }
    };

    public static Iterable<ChunkHolder> getLoadedChunksIterable(ChunkManager ref) {
        Iterable<ChunkHolder> chunkHolders;
        try {
            chunkHolders = (Iterable<ChunkHolder>)getLoadedChunksIterable.invoke(ref);
        } catch (Exception e) {
            LOGGER.error("Error on getLoadedChunksIterable Method: " + e.getMessage());
            return null;
        }
        return chunkHolders;
    }

    //Reflection Shenanigans:
    private void setupReflection() {
        if (getLoadedChunksIterable == null) {
            LOGGER.error("Chunk Reflection didn't work!");
            return;
        }
        getLoadedChunksIterable.setAccessible(true);
    }

    private static final Method getLoadedChunksIterable = getGetLoadedChunksIterable();

    private static Method getGetLoadedChunksIterable() {
        try {
            return ChunkManager.class.getDeclaredMethod("getLoadedChunksIterable");
        } catch (Exception e) {
            LOGGER.error("Exception in getGetLoadedChunksMethod: " + e.getMessage());
            return null;
        }
    }
}
