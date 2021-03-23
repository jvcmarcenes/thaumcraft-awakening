package io.github.jvcmarcenes.tca.data;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.data.server.ModBlockTagsProvider;
import io.github.jvcmarcenes.tca.data.server.ModItemTagsProvider;
import io.github.jvcmarcenes.tca.data.server.ModLootTables;
import io.github.jvcmarcenes.tca.data.server.ModRecipeProvider;
import io.github.jvcmarcenes.tca.data.client.ModBlockStateProvider;
import io.github.jvcmarcenes.tca.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        DataGenerator gen = e.getGenerator();
        ExistingFileHelper efh = e.getExistingFileHelper();

        gen.addProvider(new ModBlockStateProvider(gen, efh));
        gen.addProvider(new ModItemModelProvider(gen, efh));

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, efh);
        gen.addProvider(blockTags);
        gen.addProvider(new ModItemTagsProvider(gen, blockTags, efh));
        
        gen.addProvider(new ModRecipeProvider(gen));
        gen.addProvider(new ModLootTables(gen));
    }
}
