package io.github.jvcmarcenes.tca.world.biomes;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, TCA.MOD_ID);

    @SubscribeEvent
    public static void setupBiomes(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
            null
        );
    }

    private static void setupBiome(Biome biome, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(
            ForgeRegistries.Keys.BIOMES,
            Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null"));

        BiomeDictionary.addTypes(key, types);
        BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(key, weight));
    }

    private static class Maker {

        private static MobSpawnInfo.Builder getStandardMobSpawnBuilder() {
            MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
            DefaultBiomeFeatures.withPassiveMobs(mobspawninfo$builder);
            DefaultBiomeFeatures.withBatsAndHostiles(mobspawninfo$builder);
            return mobspawninfo$builder;
        }

    }
}
