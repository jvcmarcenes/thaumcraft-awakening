package io.github.jvcmarcenes.tca.world.gen;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static ConfiguredFeature<?, ?> CRYSTAL_CONFIG;

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        CRYSTAL_CONFIG = register("crystal_feature",
            Feature.ORE
                .withConfiguration(
                    new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                            ModBlocks.CRYSTAL_BLOCK.get().getDefaultState(), 10
                    )
                ).withPlacement(
                    new ConfiguredPlacement<>(
                        Placement.COUNT,
                        new FeatureSpreadConfig(4)
                    )
                )
        );
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }

    //@SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent event) {

    }
}
